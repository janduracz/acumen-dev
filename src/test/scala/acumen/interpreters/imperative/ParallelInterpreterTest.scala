package acumen
package interpreters
package imperative

import Pretty._

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Suite

import Common._

import java.io.FileInputStream
import java.io.InputStreamReader

class ParallelInterpreterTest extends InterpreterTestBase {

  override def interpreter : CStoreInterpreter = interpreters.imperative.ParallelInterpreter.instance

  test("StoreConversions1") {
    import ParallelInterpreter._
    val txt = """
      #0 { className = Main, parent = none, 
					 nextChild = 0, seed1 = 0, seed2 = 1 }
    """
    val cst = Parser.run(Parser.store, txt)
    val st = fromCStore(cst,CId())
    cst should be (repr(st))
  }
  
  test("StoreConversions2") {
    import ParallelInterpreter._
    val txt = """
      #0   { className = Main, parent = none, 
						 nextChild = 3, seed1 = 0, seed2 = 1 }
      #0.1 { className = A, parent = #0, o = #0.2, 
						 nextChild = 0, seed1 = 2, seed2 = 3 }
      #0.2 { className = A, parent = #0, o = #0.1, 
						 nextChild = 0, seed1 = 4, seed2 = 5 }
      """
    val cst = Parser.run(Parser.store, txt)
    val st = fromCStore(cst,CId())
    cst should be (repr(st))
  }

  test("StoreConversions3") {
    import ParallelInterpreter._
    val txt = """
#0.1 {
  parent = #0.2,
  time = 7.596000000000177,
  className = Simulator,
  resultType = @Discrete,
  endTime = 10.0,
  timeStep = 0.0030,
  nextChild = 0,
	seed1 = 0, 
	seed2 = 1
}
#0.2 {
  parent = none,
  mode = "Persist",
  className = Main,
  simulation = #0.1,
  nextChild = 0,
	seed1 = 2, 
	seed2 = 3
}
#0.4 {
  x' = 14.112000000000132,
  parent = #0.2,
  x'' = 9.8,
  x = 89.72769879999998,
  mode = "Fly",
  className = Ball,
  nextChild = 0,
	seed1 = 4, 
	seed2 = 5
}
#0.5 {
  x' = 14.112000000000132,
  parent = #0.2,
  x'' = 9.8,
  x = 89.72769879999998,
  mode = "Fly",
  className = Ball,
  nextChild = 0,
	seed1 = 6, 
	seed2 = 7
}
"""
    val cst = Parser.run(Parser.store, txt)
    val st = fromCStore(cst,CId(2))
    cst should be (repr(st))
  }

  def eqstreams(s1:Seq[CStore], s2:Seq[CStore]) : Boolean= {
    var t1 = s1
    var t2 = s2 
    var break = false
    while (t1.nonEmpty && t2.nonEmpty && !break) {
      val h1 = t1.head
      val h2 = t2.head
      t1 = t1.tail
      t2 = t2.tail
      if (h1 != h2) {
        println("*" * 30)
        println(pprint(prettyStore(h1)))
        println("!=" * 10)
        println(pprint(prettyStore(h2)))
        println("*" * 30)
        break = true
      }
    }
    if (break) false
    else t1.isEmpty && t2.isEmpty
  }

  def run(in: InputStreamReader) = {
    //val RI = interpreters.reference.Interpreter
    val PIO = ParallelInterpreter
    val ast = Parser.run(Parser.prog, in)
    val des = Desugarer().run(ast)
    for (_ <- (PIO.instance.run(des).ctrace)) ()
    //val trace1 = RI.run(des).ctrace
    //val trace2 = PIO.instance.run(des).ctrace
    //val res = eqstreams(trace1, trace2) 
    //assert(res)
  }

  testExamples({f => f.startsWith("examples/XXX_internal/test/ping-pong")})
  testShouldRun
}
