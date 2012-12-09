package acumen.interpreters.enclosure.pwl

import acumen.interpreters.enclosure.Box
import acumen.interpreters.enclosure.Interval
import acumen.interpreters.enclosure.Parameters
import acumen.interpreters.enclosure.Rounding
import acumen.interpreters.enclosure.tree.HybridSystem
import acumen.interpreters.enclosure.Types._
import acumen.interpreters.enclosure.UnivariateAffineEnclosure

trait EncloseHybrid extends EncloseEvents {

  def encloseHybrid(ps: Parameters, h: HybridSystem, t: Interval, sInit: StateEnclosure)(implicit rnd: Rounding): Seq[UnivariateAffineEnclosure] = {

    // call event localising ODE solver for each possible mode:
    val lfes: Map[Mode, LFE] = for ((mode, obox) <- sInit if obox.isDefined) yield mode -> encloseUntilEventDetected(ps, h, t, mode, obox.get)

    // extract the localised intervals from the resulting lfes:  
    val teLRs: Map[Mode, Interval] = for ((mode, (_, mae, compl)) <- lfes if compl) yield mode -> domain(mae)
    val teLs: Map[Mode, Interval] = for ((mode, (_, mae, compl)) <- lfes if !compl) yield mode -> domain(mae).low

    if (teLRs.isEmpty && teLs.isEmpty) // proved that there no event at all on T
      unionOfEnclosureLists(lfes.values.toSeq.flatMap { case (noe, _, _) => noe })
    else {
      val teLsLeftmost = teLs.values.map(_.low).toList.sortWith(_.lessThanOrEqualTo(_)).head
      val te =
        if (teLRs.isEmpty) // no potential event was fully localised on T
          teLsLeftmost /\ t.high
        else {
          val tePre = leftmostComponentOfUnion(teLRs.values.toSeq)
          if (tePre lessThan teLsLeftmost) tePre
          else tePre /\ teLsLeftmost
        }
      val noe = unionOfEnclosureLists(lfes.values.toSeq.flatMap(getNoeUntil(te.low, _)))
      val seInit: StateEnclosure = sInit.map {
        case (mode, _) => mode -> (lfes.get(mode) match {
          case Some(lfe) => Some(evalAt(lfe, te.low))
          case None => None
        })
      }
      val (se, seFinal) = encloseEvents(ps, h, te, seInit)
      if (te.high equalTo t.high) noe ++ enclosures(te, se)
      else {
        val tf = te.high /\ t.high
        val rest = encloseHybrid(ps, h, tf, seFinal)
        noe ++ enclosures(te, se) ++ rest
      }
    }
  }

  def encloseUntilEventDetected(ps: Parameters, h: HybridSystem, t: Interval, m: Mode, init: Box)(implicit rnd: Rounding): LFE =
    if (t.width greaterThan ps.maxTimeStep) repeatEncloseUntilEventDetected(ps, h, t, m, init)
    else {
      val e = solveVt(h.fields(m), t, init, ps.solveVtInitialConditionPadding, ps.extraPicardIterations, ps.maxPicardIterations, ps.splittingDegree)
      val (eLFE, eLFEisGood) = toLFE(h, e, m)
      if (t.width lessThan ps.minTimeStep * 2) eLFE
      else {
        val (tL, tR) = t.split
        val eL = solveVt(h.fields(m), tL, init, ps.solveVtInitialConditionPadding, ps.extraPicardIterations, ps.maxPicardIterations, ps.splittingDegree)
        val initR = eL(tL.high)
        val eR = solveVt(h.fields(m), tR, initR, ps.solveVtInitialConditionPadding, ps.extraPicardIterations, ps.maxPicardIterations, ps.splittingDegree)
        if (!significantImprovement(e, eR, t.right, ps.minImprovement) && eLFEisGood) eLFE
        else repeatEncloseUntilEventDetected(ps, h, t, m, init)
      }
    }

  def repeatEncloseUntilEventDetected(ps: Parameters, h: HybridSystem, t: Interval, m: Mode, init: Box)(implicit rnd: Rounding): LFE = {
    val (tL, tR) = t.split
    val lfeL = encloseUntilEventDetected(ps, h, tL, m, init)
    if (!(domain(lfeL) equalTo tL)) lfeL
    else {
      val initR = evalAtRightEndpoint(lfeL)
      val lfeR = encloseUntilEventDetected(ps, h, tR, m, initR)
      concatenate(lfeL, lfeR)
    }
  }

  def significantImprovement(eOld: UnivariateAffineEnclosure, eNew: UnivariateAffineEnclosure, x: Interval, minImprovement: Double)(implicit rnd: Rounding) = {
    val normOld = norm(eOld(x))
    val normNew = norm(eNew(x))
    normOld - normNew greaterThan minImprovement
  }

  def toLFE(h: HybridSystem, e: UnivariateAffineEnclosure, m: Mode)(implicit rnd: Rounding): (LFE, Boolean) = {
    val eRange = e.range
    if (!h.events.filter(_.sigma == m).exists(h.guards(_)(eRange).contains(true))) ((Seq(e), Seq(), false), true)
    else if (eventCertain(h, e, m)) ((Seq(), Seq(e), true), true)
    else ((Seq(), Seq(e), false), false)
  }

  def eventCertain(h: HybridSystem, e: UnivariateAffineEnclosure, m: Mode)(implicit rnd: Rounding): Boolean = {
    val eAtRightEndpoint = e(e.domain.high)
    (try { h.domains(m).support(eAtRightEndpoint); false } catch { case _ => true }) ||
      h.events.filter(_.sigma == m).exists(h.guards(_)(eAtRightEndpoint) == Set(true))
  }

  // a type for representing enclosure lists with first event 

  type LFE = (Seq[UnivariateAffineEnclosure], Seq[UnivariateAffineEnclosure], Boolean)

  // operations on LFEs

  // get the first component of `lfe` truncated at `time`
  // FIXME implement!
  def getNoeUntil(time: Interval, lfe: LFE): Seq[UnivariateAffineEnclosure] = null

  // assumes that `x` is in the domain of some enclosure in `lfe`
  // assumes that `lfe` is nonempty
  def evalAt(lfe: LFE, x: Interval)(implicit rnd: Rounding): Box = lfe match {
    case (noe, mae, _) =>
      val es = noe ++ mae
      require(es.exists(_.domain.contains(x)))
      val bs = for (e <- es if e.domain.contains(x)) yield e(x)
      bs.tail.fold(bs.head)(_ hull _)
  }

  def evalAtRightEndpoint(lfe: LFE): Box = {
    val e = lfe match { case (noe, mae, _) => (noe ++ mae).last }
    e(e.domain.high)
  }

  def domain(lfe: LFE): Interval =
    lfe match { case (noe, mae, _) => val es = noe ++ mae; es.first.domain /\ es.last.domain }

  def concatenate(lfe1: LFE, lfe2: LFE): LFE =
    (lfe1, lfe2) match {
      case ((_, _, compl1), _) if compl1 => lfe1
      case ((noe1, mae1, _), (noe2, mae2, compl2)) if mae1.isEmpty => (noe1 ++ noe2, mae2, compl2)
      case ((noe1, mae1, _), (noe2, _, _)) if !noe2.isEmpty => (noe1, mae1, true)
      case ((noe1, mae1, _), (noe2, mae2, compl2)) if noe2.isEmpty => (noe1, mae1 ++ mae2, compl2)
    }

  // auxiliary operations

  // the union of the intervals in `es` that transitively have nonempty intersection 
  // with the one(s) with least left end-point, i.e. the leftmost component of `es`
  // FIXME implement!
  def leftmostComponentOfUnion(es: Seq[Interval]): Interval = null

  // refines the enclosures so that their domains only intersect at the end-points
  // assumes that the domains of consecutive elements are connected
  // FIXME implement!
  def unionOfEnclosureLists(es: Seq[UnivariateAffineEnclosure]): Seq[UnivariateAffineEnclosure] = null

  // assumes that the domains of consecutive elements are connected
  def domain(es: Seq[UnivariateAffineEnclosure]) = {
    require(es.nonEmpty)
    es.first.domain /\ es.last.domain
  }

}
