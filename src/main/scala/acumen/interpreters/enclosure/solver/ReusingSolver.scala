package acumen.interpreters.enclosure.solver

import acumen.interpreters.enclosure.Interval
import acumen.interpreters.enclosure.Types._
import acumen.interpreters.enclosure.UnivariateAffineEnclosure
import acumen.interpreters.enclosure.Rounding
import acumen.interpreters.enclosure.Box
import acumen.interpreters.enclosure.EnclosureInterpreterCallbacks
import acumen.interpreters.enclosure.Util

trait ReusingSolver extends AtomicStep {

  def solver(
    H: HybridSystem, // system to simulate
    T: Interval, // time segment to simulate over
    Ss: Set[UncertainState], // initial modes and initial conditions
    delta: Double, // parameter of solveVt
    m: Int, // parameter of solveVt
    n: Int, // maximum number of Picard iterations in solveVt
    K: Int, // maximum event tree size in solveVtE
    minTimeStep: Double, // minimum time step size
    maxTimeStep: Double, // maximum time step size
    minImprovement: Double, // minimum improvement of enclosure
    output: String, // path to write output 
    cb: EnclosureInterpreterCallbacks)(implicit rnd: Rounding): Seq[UnivariateAffineEnclosure] = {
    Util.newFile(output)
    cb.endTime = T.hiDouble
    solveHybrid(H, delta, m, n, K, output, cb)(minTimeStep, maxTimeStep, minImprovement)(Ss, T).get._1
  }

  def solveHybrid(
    H: HybridSystem,
    delta: Double,
    m: Int,
    n: Int,
    K: Int,
    output: String,
    cb: EnclosureInterpreterCallbacks)(
      minTimeStep: Double,
      maxTimeStep: Double,
      minImprovement: Double)(
        us: Set[UncertainState],
        t: Interval)(implicit rnd: Rounding): MaybeResult = {
    if (t.width lessThanOrEqualTo maxTimeStep) {
      val maybeResultT = atomicStep(H, delta, m, n, K, output, cb.log)(us, t)
      solveHybridAux(H, delta, m, n, K, output, cb)(minTimeStep, maxTimeStep: Double, minImprovement)(us, t, maybeResultT)
    } else
      solveHybrid(H, delta, m, n, K, output, cb)(minTimeStep, maxTimeStep, minImprovement)(us, t.left) match {
        case None => None
        case Some((esl, usl)) => solveHybrid(H, delta, m, n, K, output, cb)(minTimeStep, maxTimeStep, minImprovement)(usl, t.right) match {
          case None => None
          case Some((esr, usr)) => Some(esl ++ esr, usr)
        }
      }
  }

  def solveHybridAux(
    H: HybridSystem,
    delta: Double,
    m: Int,
    n: Int,
    K: Int,
    output: String,
    cb: EnclosureInterpreterCallbacks)(
      minTimeStep: Double,
      maxTimeStep: Double,
      minImprovement: Double)(
        us: Set[UncertainState],
        t: Interval,
        maybeResultT: MaybeResult)(implicit rnd: Rounding): MaybeResult = {
    if (t.width lessThanOrEqualTo minTimeStep) maybeResultT
    else maybeResultT match {
      case None => subdivideAndRecur(H, delta, m, n, K, output, cb)(minTimeStep, maxTimeStep, minImprovement)(us, t, None)
      case Some(resultT) =>
        val (maybeResultTL, maybeResultTR) = subdivideOneLevelOnly(H, delta, m, n, K, output, cb.log)(us, t)
        if (bestOf(minImprovement)(resultT, maybeResultTR) == resultT) {
          cb.sendResult(resultT._1)
          Some(resultT)
        } else subdivideAndRecur(H, delta, m, n, K, output, cb)(minTimeStep, maxTimeStep, minImprovement)(us, t, maybeResultTL)
    }
  }

  def subdivideAndRecur(
    H: HybridSystem,
    delta: Double,
    m: Int,
    n: Int,
    K: Int,
    output: String,
    cb: EnclosureInterpreterCallbacks)(
      minTimeStep: Double,
      maxTimeStep: Double,
      minImprovement: Double)(
        us: Set[UncertainState],
        t: Interval,
        maybeResultTL: MaybeResult)(implicit rnd: Rounding): MaybeResult =
    solveHybridAux(H, delta, m, n, K, output, cb)(minTimeStep, maxTimeStep, minImprovement)(us, t.left, maybeResultTL) match {
      case None => None
      case Some((esl, usl)) =>
        solveHybrid(H, delta, m, n, K, output, cb)(minTimeStep, maxTimeStep, minImprovement)(usl, t.right) match {
          case None => None
          case Some((esr, usr)) => Some((esl ++ esr, usr))
        }
    }

  def subdivideOneLevelOnly(
    H: HybridSystem,
    delta: Double,
    m: Int,
    n: Int,
    K: Int,
    output: String,
    log: String => Unit)(
      us: Set[UncertainState],
      t: Interval)(implicit rnd: Rounding) = {
    val maybeResultTL = atomicStep(H, delta, m, n, K, output, log)(us, t.left)
    maybeResultTL match {
      case None => (None, None)
      case Some((_, usl)) =>
        val maybeResultTR = atomicStep(H, delta, m, n, K, output, log)(usl, t.right)
        (maybeResultTL, maybeResultTR)
    }
  }

}


