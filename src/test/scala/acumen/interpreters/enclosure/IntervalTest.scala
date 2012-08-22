package acumen.interpreters.enclosure

import org.scalacheck.Gen
import org.scalacheck.Gen.posNum
import org.scalacheck.Gen.sized
import org.scalacheck.Gen.choose
import org.scalacheck.Properties
import org.scalacheck.Prop._
import org.scalacheck.Arbitrary.arbitrary

import Interval.min
import Interval.max

import java.math.RoundingMode
import scala.collection.immutable.List
import scala.math.abs

import Generators._

object IntervalTest extends Properties("Interval") {

  import TestingContext._

  /* Properties */

  /**
   * Property (monotonicity w.r.t. precision): given doubles a < b and
   * positive integers i < j and intervals X = [a,b] initialized with
   * precision i and Y = [a,b] initialized with precision j it should
   * hold that the set defined by X contains the set defined by Y.
   */
  property("monotonicity w.r.t. precision") =
    forAll(arbitrary[Double], arbitrary[Double], posNum[Int], posNum[Int]) {
      (a, b, i, j) =>
        val (lo, hi) = if (a < b) (a, b) else (b, a)
        val (lop, hip) = if (i < j) (i, j) else (j, i)
        val x = Interval(lo, hi)(Rounding(lop))
        val y = Interval(lo, hi)(Rounding(hip))
        x contains y
    }

  /**
   * Property (monotonicity of functions): for each interval function f
   * it should hold that given intervals {A_1,...,A_n} and {B_1,...,B_n}
   * such that A_i is contained in B_i for each i, f(A_1,...,A_n) is
   * contained in f(B_1,...,B_n).
   */
  property("monotonicity of unary functions") =
    forAll(intervalGen, posNum[Double]) {
      (a, p) =>
        (p >= 0) ==> {
          // Unary negation
          -pad(a, p, p) contains -a
        }
    }

  property("monotonicity of binary functions (+)") = monoOfBinaryFun(_ + _)
  property("monotonicity of binary functions (-)") = monoOfBinaryFun(_ - _)
  property("monotonicity of binary functions (*)") = monoOfBinaryFun(_ * _)
  property("monotonicity of binary functions (/)") =
    forAll(for {
      val a <- intervalGen
      val b <- intervalGen suchThat { i => !i.contains(0) }
    } yield (a, b)) {
      case ((al, ar)) =>
        forAll(
          posNum[Double], posNum[Double],
          choose[Double](0.0, ar.lo.doubleValue), posNum[Double]) {
            (lLoPad, lHiPad, rLoPad, rHiPad) =>
              val bl = pad(al, lLoPad, lHiPad)
              val br = pad(ar, if (rLoPad > 0) rLoPad else -rLoPad, rHiPad)
              (bl / br) contains (al / ar)
          }
    }

  property("monotonicity of binary functions (/\\)") = monoOfBinaryFun((l, r) => l /\ r)
  property("monotonicity of binary functions (\\/)") =
    forAll(
      arbitrary[Double], arbitrary[Double], arbitrary[Double], arbitrary[Double],
      posNum[Double], posNum[Double], posNum[Double], posNum[Double]) {
        case (p1, p2, p3, p4, lLoPad, lHiPad, rLoPad, rHiPad) =>
          val lst = List(p1, p2, p3, p4).sorted
          val al = Interval(lst(0), lst(2))
          val ar = Interval(lst(1), lst(3))
          val bl = pad(al, lLoPad, lHiPad)
          val br = pad(ar, rLoPad, rHiPad)
          (bl \/ br) contains (al \/ ar)
      }

  /**
   * /\ is an approximation of the superset-meet in the interval lattice. The meet
   * itself may not be exactly representable, so we get some lesser lower bound
   * instead.
   *
   * property: for any intervals A and B it holds that A /\ B contains both A and B.
   */
  property("/\\ on Intervals is the interval union") =
    forAll(intervalGen, intervalGen) { (a, b) =>
      (a /\ b contains a) && (a /\ b contains b)
    }

  /* Utilities */

  /** Checks that the binary function "f" is monotonic, i.e. that if A contains B then f(A) contains f(B). */
  def monoOfBinaryFun(f: (Interval, Interval) => Interval) = monoOfBinaryFunG(f, genIntervalPair)

  def genIntervalPair = for { a <- arbitrary[Interval]; b <- arbitrary[Interval] } yield (a, b)

  def monoOfBinaryFunG(f: (Interval, Interval) => Interval, g: Gen[(Interval, Interval)]) =
    forAll(g, posNum[Double], posNum[Double], posNum[Double], posNum[Double]) {
      case ((al, ar), lLoPad, lHiPad, rLoPad, rHiPad) =>
        val bl = pad(al, lLoPad, lHiPad)
        val br = pad(ar, rLoPad, rHiPad)
        f(bl, br) contains f(al, ar)
    }

}