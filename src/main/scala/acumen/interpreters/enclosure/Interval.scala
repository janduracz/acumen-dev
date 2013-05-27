package acumen.interpreters.enclosure

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import Interval._
import net.sourceforge.interval.ia_math.IAMath
import net.sourceforge.interval.ia_math.RealInterval

/**
 * Intervals with outward-rounded operations.
 *
 * Outward-rounded means that the low end-point is rounded downwards
 * and the high end-point is rounded upwards.
 *
 * The end-points are parameterized by precision, i.e. the number of
 * digits in the decimal expansion of the end-point value.
 *
 * Precondition: lo <= hi.
 *
 * property (monotonicity w.r.t. precision): given doubles a < b and
 * positive integers i < j and intervals X = [a,b] initialized with
 * precision i and Y = [a,b] initialized with precision j it should
 * hold that the set defined by X contains the set defined by Y.
 *
 * property (monotonicity of functions): for each interval function f
 * it should hold that given intervals {A_1,...,A_n} and {B_1,...,B_n}
 * such that A_i is contained in B_i for each i, f(A_1,...,A_n) is
 * contained in f(B_1,...,B_n).
 */
case class Interval(
    val lo: Real,
    val hi: Real)(implicit val rnd: Rounding) {
  import rnd._

  def low = Interval(lo)

  def high = Interval(hi)

  def bounds = (low, high)

  def midpoint = hi.subtract(lo, dn).divide(Interval(2).lo, dn).add(lo, dn)

  def left = Interval(lo, midpoint)

  def right = Interval(midpoint, hi)

  /**
   * Set difference.
   *
   * Note: yields None for empty intersections.
   */
  def setminus(that: Interval): Option[Interval] =
    if (that contains this) None
    else Some(this \ that)

  /**
   * Set difference.
   *
   * Note: partial operation, fails for empty intersections.
   */
  def \(that: Interval): Interval = {
    require(!(that contains this), "Interval.\\: cannot produce empty set difference!")
    if (that contains lo) Interval(that.hi, hi)
    else if (that contains hi) Interval(lo, that.lo)
    else this
  }

  def intersect(that: Interval): Option[Interval] =
    if (this disjointFrom that) None
    else Some(this \/ that)

  def split = {
    val mid = midpoint
    (Interval(lo, mid), Interval(mid, hi))
  }

  def refine(pieces: Int) = {
    val mesh = width.lo.divide(Interval(pieces).hi, dn)
    (0 until pieces - 1).map {
      i =>
        Interval(lo.add(mesh.multiply(Interval(i).lo, dn), dn),
          lo.add(mesh.multiply(Interval(i + 1).lo, dn), dn))
    } :+ Interval(lo.add(mesh.multiply(Interval(pieces - 1).lo, dn), dn), hi)
  }

  /** Interval of absolute values of elements in this interval. */
  def abs = Interval.max(this /\ -this, Interval(0))

  /**
   * Interval of n:th power values of elements in this interval.
   *
   * @precondition n >= 0.
   */
  def pow(n: Int): Interval = {
    require(n >= 0)
    val h = hi.pow(n, rnd.up)
    val l = lo.pow(n, rnd.dn)
    val res = Interval(l) /\ Interval(h)
    val result =
      if ((n % 2 != 0) || !(this contains 0)) res
      else Interval(0) /\ res
    result
  }

  /** Interval of possible square roots elements in this interval. */
  def sqrt = {
    def sqrt(dir: java.math.MathContext)(x: Real) = {
      val half = new Real(0.5, dir)
      val xinit = x.round(dir)
      var res = xinit
      var tmp = xinit.subtract(xinit, dir) // == 0
      while (res != tmp) {
        tmp = res
        res = half.multiply(res.add(xinit.divide(res, dir), dir), dir)
      }
      res
    }
    if (this lessThan Interval(0)) sys.error("sqrt is undefined on " + this)
    else Interval(sqrt(dn)(max(lo, lo.subtract(lo))), sqrt(up)(hi))
  }

  /**
   * Interval of possible values of the exponential function over this interval.
   *
   * FIXME: need to check that truncation is accounted for!
   */
  def exp(implicit rnd: Rounding) = {
    val zero = Interval(0)
    val one = Interval(1)
    val unit = Interval(-1, 1)
    // scales this interval into [-1,1] 
    // returns scaled interval and the power to take when scaling back
    def scaleDownByHalving(x: Interval): (Interval, Int) = {
      var result = x
      var power = 1
      while (!(unit contains result)) {
        result /= 2
        power *= 2
      }
      val res = (result, power)
      res
    }
    // expects thin x
    def expThin(x: Interval) = {
      require(x.low == x.high, x + " should be thin!")
      // expects thin nonnegative x
      def expThinNonneg(x: Interval) = {
        require(x greaterThanOrEqualTo zero)
        if (x contains 0) one
        else {
          // expects degree > 1
          def expTaylor(x: Interval, degree: Int): Interval = {
            require(degree > 1)
            var pow = x
            var res = one + pow
            for (i <- 2 to degree) {
              pow *= x / i
              res += pow
            }
            res
          }
          // arbitrarily chosen degree to be odd and grow with precision
          expTaylor(x, 2 * rnd.precision + 1)
        }
      }
      if (x lessThan zero) one / expThinNonneg(-x)
      else expThinNonneg(x)
    }
    val (scaledLow, halvingsLow) = scaleDownByHalving(low)
    val (scaledHigh, halvingsHigh) = scaleDownByHalving(high)
    val scaledLowExp = expThin(scaledLow.low)
    val scaledHighExp = expThin(scaledHigh.high)
    val resLow = scaledLowExp.low.pow(halvingsLow).low
    val resHigh = scaledHighExp.high.pow(halvingsHigh).high
    resLow /\ resHigh
  }

  /**
   * Adds that interval to this one.
   *  @param that the interval to add.
   *  @return an interval which is the subset-least upper bound
   *  of the sum of this and that and representable in the given
   *  precision.
   */
  def +(that: Interval) = Interval(lo.add(that.lo, dn), hi.add(that.hi, up))

  /**
   * Subtracts that interval from this one.
   *  @param that the interval to subtract.
   *  @return an interval which is the subset-least upper bound
   *  of the difference of this and that and representable in the
   *  given precision.
   */
  def -(that: Interval) = Interval(lo.subtract(that.hi, dn), hi.subtract(that.lo, up))

  /**
   * Negation of this interval.
   *  @return an interval which is the negation of this.
   */
  def unary_- = Interval(hi.negate, lo.negate)

  /**
   * Multiplies that interval with this one.
   *  @param that the interval to multiply with.
   *  @return an interval which is the subset-least upper bound
   *  of the product of this and that and representable in the
   *  given precision.
   */
  def *(that: Interval) = {
    val prodsDN: List[Real] = List(lo.multiply(that.hi, dn), hi.multiply(that.lo, dn), hi.multiply(that.hi, dn))
    val prodsUP: List[Real] = List(lo.multiply(that.hi, up), hi.multiply(that.lo, up), hi.multiply(that.hi, up))
    Interval(prodsDN.foldLeft(lo.multiply(that.lo, dn))(min(_, _)), prodsUP.foldLeft(lo.multiply(that.lo, up))(max(_, _)))
  }

  def *(that: Double): Interval = this * Interval(that)

  /** Squares this interval. */
  def square = max(Interval(0), this * this)

  /**
   * Divides this interval by that one.
   *  @param that the divisor.
   *  @return an interval which is the subset-least upper bound
   *  of the quotient of this and that and representable in the
   *  given precision.
   */
  def /(that: Interval) = {
    require(!that.contains(0), "division by 0")
    val divsDN: List[Real] = List(lo.divide(that.hi, dn), hi.divide(that.lo, dn), hi.divide(that.hi, dn))
    val divsUP: List[Real] = List(lo.divide(that.hi, up), hi.divide(that.lo, up), hi.divide(that.hi, up))
    Interval(divsDN.foldLeft(lo.divide(that.lo, dn))(min(_, _)), divsUP.foldLeft(lo.divide(that.lo, up))(max(_, _)))
  }

  def /(that: Double): Interval = this / Interval(that)

  /**
   * Take the meet, or g.l.b., of this and that interval.
   *  @param that the interval to take the the meet with.
   *  @return an interval which is the interval-wrapped set
   *  union of this and that.
   *
   *  property: for any intervals A and B it holds that A /\ B
   *  contains both A and B.
   */
  def /\(that: Interval) = Interval(min(lo, that.lo), max(hi, that.hi))

  /**
   * Take the join, or l.u.b., of this and that interval.
   * The intersection of this and that must be non-empty!
   *  @param that the interval to take the the join with.
   *  @return an interval which is the intersection of this
   *  and that.
   */
  def \/(that: Interval) = {
    require(!(this disjointFrom that), "cannot intersect disjoint intervals " + this + " and " + that)
    Interval(max(lo, that.lo), min(hi, that.hi))
  }

  def disjointFrom(that: Interval) =
    that.hi.compareTo(this.lo) < 0 || this.hi.compareTo(that.lo) < 0

  /**
   * The width of this interval.
   * @return the least interval containing this.hi-this.lo
   *
   * property: for any interval X it holds that X.width >= 0.
   *
   * property: for any interval X it holds that (-X).width == X.width.
   */
  def width = Interval(hi.subtract(lo, dn), hi.subtract(lo, up))

  /**
   * Comparison operations on intervals. WARNING!
   * They are not each other's negations. E.g.
   * [0,2] lessThan [1,3] == false but
   * [1,3] greaterThanOrEqualTo [0,2] == false as well!
   * Do not form expressions by negating values returned by these
   * methods, they will likely not produce to the intended results.
   *
   * property (positive monotonicity): for any intervals A,B,C,D such
   * that A contains B and C contains D it holds that if A lessThan C
   * is true then B lessThan D is true.
   */
  def lessThan(that: Interval) = hi.compareTo(that.lo) < 0

  /**
   * property (positive monotonicity): for any intervals A,B,C,D such
   * that A contains B and C contains D it holds that if A lessThanOrEqualTo C
   * is true then B lessThanOrEqualTo D is true.
   */
  def lessThanOrEqualTo(that: Interval) = hi.compareTo(that.lo) <= 0

  /**
   * property (positive monotonicity): for any intervals A,B,C,D such
   * that A contains B and C contains D it holds that if A greaterThanOrEqualTo C
   * is true then B greaterThanOrEqualTo D is true.
   */
  def equalTo(that: Interval) = (this.lo compareTo that.lo) == 0 && (this.hi compareTo that.hi) == 0

  def greaterThanOrEqualTo(that: Interval) = lo.compareTo(that.hi) >= 0

  /**
   * property (positive monotonicity): for any intervals A,B,C,D such
   * that A contains B and C contains D it holds that if A greaterThan C
   * is true then B greaterThan D is true.
   */
  def greaterThan(that: Interval) = lo.compareTo(that.hi) > 0

  /**
   * Determine if this interval contains that interval.
   * @param that the interval to test containment of.
   *  @return a boolean that is true if and only if the underlying
   *  set of this contains the underlying set of that and false
   *  otherwise. Note that a false result does not imply the opposite
   *  relation holds!
   */
  def contains(that: Interval) = lo.compareTo(that.lo) <= 0 && that.hi.compareTo(hi) <= 0

  def contains(x: Real) = lo.compareTo(x) <= 0 && x.compareTo(hi) <= 0

  def contains(x: Double) = {
    val it = Interval(x)
    lo.compareTo(it.lo) <= 0 && it.hi.compareTo(hi) <= 0
  }

  // FIXME improve this
  def properlyContains(that: Interval): Boolean =
    contains(that) && (lo.compareTo(that.lo) < 0 || that.hi.compareTo(hi) < 0)

  def isThin = (lo compareTo hi) == 0

  def isZero = equalTo(Interval(0))
  def isNonnegative = greaterThanOrEqualTo(Interval(0))

  def almostEqualTo(that: Interval) = {
    //    println("almostEqualTo: " + epsilon + " contains " + (this.low - that.low) + " is " + (epsilon contains (this.low - that.low)))
    //    println("almostEqualTo: " + epsilon + " contains " + (this.high - that.high) + " is " + (epsilon contains (this.high - that.high)))
    (epsilon contains (this.low - that.low)) &&
      (epsilon contains (this.high - that.high))
  }

  /**
   * @return a string representation of the interval in the usual
   * notation for closed intervals.
   */
  override def toString = "[" + lo + "," + hi + "]"

  /** Utilities for safe cast to Double interval. */
  private lazy val dnToDouble = new java.math.MathContext(15, java.math.RoundingMode.FLOOR)
  private lazy val upToDouble = new java.math.MathContext(15, java.math.RoundingMode.CEILING)

  def loDouble: Double = lo.round(dnToDouble).doubleValue()
  def hiDouble: Double = hi.round(upToDouble).doubleValue()

  /** Interval function wrappers for IAMath functions. */

  def cos: Interval = {
    val value = IAMath.cos(new RealInterval(loDouble, hiDouble))
    require(!value.lo.isNaN && !value.hi.isNaN)
    Interval(value.lo, value.hi)
  }

  /**
   * Inverse cosine, currently added for back-propagation over cosine.
   *
   * Precondition: this.abs in [-1,1] (inherited from java.math.Math.acos)
   */
  def acos: Interval = {
    require(Interval(-1, 1) contains this)
    val value = IAMath.acos(new RealInterval(loDouble, hiDouble))
    require(!value.lo.isNaN && !value.hi.isNaN)
    Interval(value.lo, value.hi)
  }

}

object Interval {
  import java.math.BigDecimal
  type Real = java.math.BigDecimal

  def apply(lo: Double, hi: Double)(implicit rnd: Rounding): Interval =
    Interval(new BigDecimal(lo, rnd.dn), new BigDecimal(hi, rnd.up))
  def apply(x: Int)(implicit rnd: Rounding): Interval = Interval(x, x)
  def apply(x: Double)(implicit rnd: Rounding): Interval = Interval(x, x)
  def apply(x: Real)(implicit rnd: Rounding): Interval = Interval(x, x)
  def min(left: Interval, right: Interval)(implicit rnd: Rounding): Interval =
    Interval(min(left.lo, right.lo), min(left.hi, right.hi))
  def max(left: Interval, right: Interval)(implicit rnd: Rounding): Interval =
    Interval(max(left.lo, right.lo), max(left.hi, right.hi))
  def min(left: Real, right: Real) = if (left.compareTo(right) < 0) left else right
  def max(left: Real, right: Real) = if (left.compareTo(right) > 0) left else right

  implicit def toInterval(x: Real)(implicit rnd: Rounding) = Interval(x)
  implicit def toInterval(x: Double)(implicit rnd: Rounding) = Interval(x)
  implicit def toInterval(x: Int)(implicit rnd: Rounding) = Interval(x)
  def epsilon(implicit rnd: Rounding) = {
    val eps = new Real(0.1).pow(rnd.precision - 1, rnd.up)
    Interval(eps.negate, eps)
  }

  /* Constants */

  def pi(implicit rnd: Rounding): Interval = {
    require(rnd.precision <= 1000, "pi constant only supported up to precision 1000.")
    return Interval(pi1000 round rnd.dn, pi1000 round rnd.up) // TODO Memoize
  }
  private val pi1000 = new BigDecimal(
    "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679" +
      "821480865132823066470938446095505822317253594081284811174502841027019385211055596446229489549303819644" +
      "288109756659334461284756482337867831652712019091456485669234603486104543266482133936072602491412737245" +
      "870066063155881748815209209628292540917153643678925903600113305305488204665213841469519415116094330572" +
      "703657595919530921861173819326117931051185480744623799627495673518857527248912279381830119491298336733" +
      "624406566430860213949463952247371907021798609437027705392171762931767523846748184676694051320005681271" +
      "452635608277857713427577896091736371787214684409012249534301465495853710507922796892589235420199561121" +
      "290219608640344181598136297747713099605187072113499999983729780499510597317328160963185950244594553469" +
      "083026425223082533446850352619311881710100031378387528865875332083814206171776691473035982534904287554" +
      "687311595628638823537875937519577818577805321712268066130019278766111959092164201989380952572010654858" +
      "632788659361533818279682303019520353018529689957736225994138912497217752834791315155748572424541506959")

  def union(is: Seq[Interval]): Interval =
    if (is.isEmpty) sys.error("Interval.union: empty union")
    else is.tail.fold(is.head)(_ /\ _)

  def max(is: Iterable[Interval])(implicit rnd: Rounding): Interval = {
    require(is.nonEmpty)
    is.tail.fold(is.head)(max(_, _))
  }

}

object IntervalApp extends App {

  implicit val rnd = Rounding(10)

}
