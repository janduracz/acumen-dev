package acumen
package interpreters
package optimized

import scala.collection.immutable.HashMap

import acumen.Errors._
import acumen.Pretty._
import acumen.util.Conversions._
import acumen.util.Random
import acumen.interpreters.Common._
import acumen.util.Canonical.{
  childrenOf, 
  classf,
  cmain,
  cmagic,
  endTime,
  nextChild,
  parentOf,
  parent,
  seedOf,
  seed1,
  seed2,
  self,
  resultType,
  time,
  timeStep
}
import scala.annotation.tailrec

abstract class ContMode;
object ContMode {
  case object Seq extends ContMode
  case object Par extends ContMode
  case object IVP extends ContMode
}

class Interpreter(val parDiscr: Boolean = true, 
                  val contMode: ContMode = ContMode.Seq,
                  val contWithDiscr: Boolean = false) extends CStoreInterpreter {
  override def id = Array("optimized", 
                          if (parDiscr) "parDiscr" else "seqDiscr",
                          contMode match {case ContMode.Seq => "seqCont"; 
                                          case ContMode.Par => "parCont"; 
                                          case ContMode.IVP => "IVP";},
                          if (contWithDiscr) "contWithDiscr" else "contWithCont")

  import Common._ 

  type Store = Common.Store
  def repr (s:Store) : CStore = Common.repr(s)
  def fromCStore (cs:CStore, root:CId) : Store = Common.fromCStore(cs, root)

  def init(prog: Prog): (Prog, Store) = {
    val magic = fromCStore(initStoreImpr, CId(0))
    val (sd1, sd2) = Random.split(Random.mkGen(0))
    val mainObj = mkObj(cmain, prog, IsMain, sd1, List(VObjId(Some(magic))), magic, 1)
    magic.seed = sd2
    val cprog = CleanParameters.run(prog, CStoreInterpreterType)
    val sprog = Simplifier.run(cprog)
    val mprog = Prog(magicClass :: sprog.defs)
    (mprog , mainObj)
  }

  def localStep(p: Prog, st: Store): ResultType = {
    val magic = getSimulator(st)
    stepInit
  
    val pp = magic.phaseParms
    pp.curIter += 1
  
    if (getTime(magic) > getEndTime(magic)) {

      null

    } else {

      pp.gatherEquationI = false
      val rt = if (getResultType(magic) != FixedPoint) { // Discrete Step

        if (parDiscr) pp.delayUpdate = true
        else          pp.delayUpdate = false
        pp.doDiscrete = true
        if (contWithDiscr) pp.doEquationT = true
        else               pp.doEquationT = false
        pp.doEquationI = false

        traverse(evalStep(p, magic), st) match {
          case SomeChange(dead, rps) =>
            for ((o, p) <- rps)
            changeParent(o, p)
            for (o <- dead) {
              o.parent match {
                case None => ()
                case Some(op) =>
                  for (oc <- o.children) changeParent(oc, op)
                  op.children = op.children diff Seq(o)
              }
            }
            Discrete
          case NoChange() =>
            FixedPoint
          }

      } else if (contMode != ContMode.IVP) { // Continuous step 

        if (contMode == ContMode.Par) pp.delayUpdate = true
        else                          pp.delayUpdate = false
        pp.doDiscrete = false
        if (contWithDiscr) pp.doEquationT = false
        else               pp.doEquationT = true
        pp.doEquationI = true

        traverse(evalStep(p, magic), st)

        Continuous

      } else { // Continuous step, IVP mode

        pp.delayUpdate = true
        pp.doDiscrete = false
        pp.doEquationT = true
        pp.doEquationI = false
        pp.gatherEquationI = true
        pp.odes.clear()
        
        traverse(evalStep(p, magic), st)
        
        val sz = pp.odes.size
        val initVal = new Array[Val](sz)
        var idx = 0
        while (idx < sz) {
          val eqt = pp.odes(idx)
          initVal(idx) = eqt.id.fields(eqt.field).prevVal
          idx += 1
        }

        implicit val field = FieldImpl(pp.odes, p)
        val res = solveIVPRungeKutta(initVal : IndexedSeq[Val], getTimeStep(magic))
        idx = 0
        while (idx < sz) {
          val eqt = pp.odes(idx)
          setFieldSimple(eqt.id, eqt.field, res(idx))
          idx += 1
        }

        Continuous
      }

      if (rt == Continuous)
        setTime(magic, getTime(magic) + getTimeStep(magic))
      setResultType(magic, rt)

      rt
    }
  }

  def step(p: Prog, st: Store): Option[Store] = {
    val res = localStep(p, st)
    if (res == null) None
    else Some(st)
  }

  // always returns the last known step, the adder callback is used to
  // determine when teh simulation is done
  override def multiStep(p: Prog, st: Store, adder: DataAdder): Store = {
    val magic = getSimulator(st)
    var shouldAddData = ShouldAddData.IfLast
    // ^^ set to IfLast on purpose to make things work
    @tailrec def step0() : Unit = {
      val res = localStep(p, st)
      stepInit
      if (res == null) {
        if (shouldAddData == ShouldAddData.IfLast)
          addData(st, adder)
        adder.noMoreData()
      } else {
        shouldAddData = adder.newStep(res)
        if (shouldAddData == ShouldAddData.Yes)
          addData(st, adder)
        if (adder.continue)
          step0()
      }
    }
    step0()
    st
  }

  def addData(st: Store, adder: DataAdder) : Unit = {
    // Note: Conversion to a CStore just to add the data is certainly
    // not the most efficient way to go about things, but for now it
    // will do. --kevina
    adder.addData(st.id, st.fieldsCur)
    st.children.foreach { child => addData(child, adder) }
  }

  def stepInit : Unit = {}
  def traverse(f: ObjId => Changeset, root: ObjId): Changeset =
    traverseSimple(f, root)

}

object Interpreter extends Interpreter(true,ContMode.Seq,false)

