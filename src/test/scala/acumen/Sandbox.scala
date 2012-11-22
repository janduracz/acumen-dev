package acumen

import acumen.interpreters.Common.classDef
import acumen.interpreters.enclosure.solver.Solver.defaultCallback
import acumen.interpreters.enclosure.solver.FixedStepSolver
import acumen.interpreters.enclosure.solver.HybridSystem
import acumen.interpreters.enclosure.Extract
import acumen.interpreters.enclosure.Rounding
import acumen.interpreters.enclosure.solver.Solver
import acumen.interpreters.enclosure.solver.SolveVt
import acumen.interpreters.enclosure.Variable
import acumen.interpreters.enclosure.solver.Field
import acumen.interpreters.enclosure.Interval
import acumen.interpreters.enclosure.Box
import TransformTest._
import java.awt.Color
import acumen.interpreters.enclosure.UnivariateAffineEnclosure

object Sandbox extends App with Extract with Solver with SolveVt {

  //  val prog = Parser.run(Parser.prog, Models("bouncing_ball_convergent"))
  val prog = Parser.run(Parser.prog, Models.fullers_phenomenon)
  val des = Desugarer.run(prog)
  val main = classDef(ClassName("Main"), des)

  val ps = parameters(main)
  implicit val rnd = Rounding(ps.precision)
  val (h: HybridSystem, us) = extract(main)

  val start = System.currentTimeMillis

  val res =
    solver(
      h,
      ps.simulationTime,
      Set(us),
      ps.solveVtInitialConditionPadding,
      ps.extraPicardIterations,
      ps.maxPicardIterations,
      ps.splittingDegree,
      ps.maxEventTreeSize,
      ps.minTimeStep,
      ps.maxTimeStep,
      ps.minImprovement,
      "output",
      defaultCallback)

  val end = System.currentTimeMillis
  val time = end - start
  println("computed " + res.size + " enclosures in " + time / 1000.0 + " seconds")

  def twoMinusExpOfMinusT(t: Double) = 2 - scala.math.exp(-t) // DELETEME
  def fiveMinusFiveTSquare(t: Double) = 5 - 5 * scala.math.pow(t, 2) // DELETEME

  val plotter = new acumen.interpreters.enclosure.Plotter
  plotter.plot("Plotter mockup")(null)(res)(new Rounding(10))

}