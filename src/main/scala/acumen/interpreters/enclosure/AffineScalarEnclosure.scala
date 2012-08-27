package acumen.interpreters.enclosure

import Types._
import Util._

/**
 * Type used to approximate expressions over a given domain.
 *
 * Implementation note: each variable is stored as a projection over an
 * interval of the form [0,_]. This simplifies the implementation of
 * enclosure operations because variables become monotonically increasing
 * over such intervals, which e.g. allows for easy extraction of the bounds
 * of the enclosure.
 */
case class AffineScalarEnclosure private[enclosure] (
  private[enclosure]domain: Box,
  /* To save wasteful shifting during enclosure operations the internal
   * representation of the domain is such that each variable is non-negative.
   * 
   * Implementation note: this will make it possible to e.g. compute bounds 
   * of the enclosure by simply taking the corresponding bounds of the 
   * constant term and coefficients. */
  private[enclosure]normalizedDomain: Box,
  private[enclosure]constant: Interval,
  private[enclosure]coefficients: Box) {
  assert(coefficients.keySet subsetOf domain.keySet, "The variable of each coefficient must occur in the domain.")
  assert(domain.keySet == normalizedDomain.keySet, "The variables of the normalized domain must coincide with those of the domain.")

  /** The number of variables the enclosure depends on. */
  def arity = coefficients.size

  /** The number of variables in the domain of the enclosure. */
  def dimension = domain.size

  /** The low bound enclosure of this enclosure. */
  def low = AffineScalarEnclosure(domain, normalizedDomain, constant.low, coefficients.mapValues(_.low))

  /** The high bound enclosure of this enclosure. */
  def high = AffineScalarEnclosure(domain, normalizedDomain, constant.high, coefficients.mapValues(_.high))

  /**
   * Get the linear terms of this enclosure.
   *
   * Implementation note: this is achieved buy setting the constant term
   * to [0,0].
   */
  def linearTerms(implicit end: Rounding) = AffineScalarEnclosure(domain, normalizedDomain, 0, coefficients)

  /** Get the constant term of this enclosure. */
  def constantTerm(implicit end: Rounding) = AffineScalarEnclosure(domain, normalizedDomain, constant, Box())

  /**
   * Evaluate the enclosure at the box x.
   *
   * Precondition: the box must have a domain for each coefficient name.
   *
   * Note that the box should be component-wise contained within the domain
   * of this enclosure for the approximation guarantees to hold.
   *
   * E.g. [t-0.25,t] is a valid enclosure of t^2 over [0,1] but not outside
   * [0,1]. E.g. [1.75,2] does not contain the value 4 of t^2 at t=2.
   *
   * Implementation note: the algorithm relies on the extreme values of the
   * enclosure occurring at the corners of the box. This is the case for e.g.
   * affine enclosures.
   */
  def apply(x: Box)(implicit rnd: Rounding) = {
    assert(coefficients.keySet subsetOf x.keySet,
      "An enclosure can only be evaluated over a box that has a domain for each variable.")
    /* It is essential to evaluate the enclosure over the original domain.
     * To avoid unnecessary errors the argument is shifted to the normalized
     * domain rather than the enclosure to the original domain. 
     * 
     * Implementation note: cannot use normalize because it does not take the 
     * current domain of the enclosure into consideration. E.g. normalize maps
     * thin intervals to [0,0]! */
    val c :: cs = Box.corners(x.map {
      case (name, value) => name -> (value - domain(name).low)
    })
    val lo = low
    val hi = high
    cs.foldLeft((lo evalThinAtThin c) /\ (hi evalThinAtThin c)) {
      case (res, corner) => res /\ (lo evalThinAtThin corner) /\ (hi evalThinAtThin corner)
    }
  }
  /**
   * Naive evaluation of this enclosure at a box.
   *
   * Precondition: the box x must be component-wise within the normalized
   * domain of this enclosure.
   *
   * To minimize errors due to interval arithmetic information loss both this
   * interval and the box should be thin, i.e. each interval should have zero
   * width.
   */
  private def evalThinAtThin(x: Box)(implicit rnd: Rounding): Interval = {
    assert(x.forall { case (name, interval) => normalizedDomain(name) contains interval },
        "The argument must be contained in the normalized domain.")
    coefficients.foldLeft(constant) { case (res, (name, coeff)) => res + coeff * x(name) }
  }

  /**
   * Get the range of the enclosure.
   *
   * Since the enclosure is a safe approximation of any contained function
   * the range also safely approximates the range of any such function.
   */
  def range(implicit rnd: Rounding): Interval = this(domain)

  /**
   * Produce an enclosure without the variable "name" that approximates this enclosure.
   *
   * Implementation note: the domain of name is assumed to be non-negative.
   *
   * property: (monotonicity of domain collapsing): The enclosure which is being collapsed
   * must be point-wise included in the enclosure obtained by collapsing.
   */
  private def collapse(name: VarName)(implicit rnd: Rounding) = {
    println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
    println("AffineScalarEnclosure.collapse: entry")    
    println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
    println("name to collapse:         " + name)
    println("domain before collapsing: " + domain)
    val res = AffineScalarEnclosure(
      domain - name,
      normalizedDomain - name,
      constant + coefficients.getOrElse(name, Interval(0)) * normalizedDomain.getOrElse(name, Interval(0)),
      coefficients - name)
    println("domain after collapsing:  " + res.domain)
    println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
    println("AffineScalarEnclosure.collapse: exit")
    println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
    res
  }

  def collapse(names: VarName*)(implicit rnd: Rounding): AffineScalarEnclosure = {
    println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
    println("AffineScalarEnclosure.collapse*: entry")
    println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
    println("names to collapse:        " + names)
    println("domain before collapsing: " + domain)
    val res = names.foldLeft(this)((res, name) => res.collapse(name))
    println("domain after collapsing:  " + res.domain)
    println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
    println("AffineScalarEnclosure.collapse*: exit")
    println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
    res
  }

  /**
   * Containment of enclosures.
   *
   * Implementation note: representing enclosures over normalized domains
   * allows us to test containment by constant- and coefficient-wise
   * containment.
   */
  def contains(that: AffineScalarEnclosure)(implicit rnd: Rounding) = {
    val lodiffnonneg = (low - that.low).range lessThanOrEqualTo Interval(0)
    val hidiffnonneg = (that.high - high).range lessThanOrEqualTo Interval(0)
    lodiffnonneg && hidiffnonneg
  }

  /** Pads the enclosure by delta. The result is an enclosure that contains this enclosure. */
  def plusMinus(delta: Interval)(implicit rnd: Rounding) = this + (-delta) /\ delta
  def plusMinus(delta: Double)(implicit rnd: Rounding): AffineScalarEnclosure =
    this plusMinus Interval(delta)

  /* Arithmetic operations */

  /** Negation of enclosures. */
  def unary_-(implicit r: Rounding): AffineScalarEnclosure =
    AffineScalarEnclosure(domain, normalizedDomain, -constant, coefficients.mapValues(-_))

  /**
   * Creates a new enclosure by applying a binary operator to the values of "this" and "that" enclosure.
   * In the cases when one of the enclosures does not contain a variable which the other does, the
   * interval [0,0] is used as a default.
   */
  private def zipWith(f: (Interval, Interval) => Interval)(that: AffineScalarEnclosure)(implicit rnd: Rounding) =
    AffineScalarEnclosure(domain, normalizedDomain,
      f(constant, that.constant),
      (this.coefficients.keySet union that.coefficients.keySet).map { k =>
        (k, f(
          this.coefficients.getOrElse(k, Interval(0)),
          that.coefficients.getOrElse(k, Interval(0))))
      }.toMap)

  /** Addition of enclosures. */
  def +(that: AffineScalarEnclosure)(implicit rnd: Rounding): AffineScalarEnclosure = zipWith(_ + _)(that)
  def +(that: Interval)(implicit rnd: Rounding): AffineScalarEnclosure = AffineScalarEnclosure(domain, normalizedDomain, constant + that, coefficients)
  def +(that: Double)(implicit rnd: Rounding): AffineScalarEnclosure = this + Interval(that)
  def +(that: Int)(implicit rnd: Rounding): AffineScalarEnclosure = this + Interval(that)

  /** Subtraction of enclosures. */
  def -(that: AffineScalarEnclosure)(implicit rnd: Rounding): AffineScalarEnclosure = zipWith(_ - _)(that)
  def -(that: Interval)(implicit rnd: Rounding): AffineScalarEnclosure = AffineScalarEnclosure(domain, normalizedDomain, constant - that, coefficients)
  def -(that: Double)(implicit rnd: Rounding): AffineScalarEnclosure = this - Interval(that)
  def -(that: Int)(implicit rnd: Rounding): AffineScalarEnclosure = this - Interval(that)

  /**
   * Multiplication of enclosures.
   *
   * The case of multiplying two enclosures is computed as follows:
   *
   * (a_0 + a_1*x_1 + ... + a_n*x_n) * (b_0 + b_1*x_1 + ... + b_n*x_n)
   * ==
   * a_0*b_0 +
   * a_0*sum{ b_i*x_i | 1 <= i <= n } + b_0*sum{ a_i*x_i | 1 <= i <= n } +
   * sum{ a_i*b_i*x_i^2 | 1 <= i <= n } +
   * sum{ a_i*b_j*x_i*x_j | 1 <= i,j <= n and i != j }
   */
  def *(that: AffineScalarEnclosure)(implicit rnd: Rounding): AffineScalarEnclosure = {
    assert(domain == that.domain, "Multiplication is defined on enclosures with identical domains.")
    val const = constant * (that.constant)
    val linear = (that.linearTerms * constant) + (this.linearTerms * that.constant)
    val square = domain.keySet.map(name => quadratic(name) * coefficients(name) * that.coefficients(name))
    val mixeds = (for (name1 <- domain.keys; name2 <- domain.keys if name1 != name2) yield mixed(name1, name2) * coefficients(name1) * that.coefficients(name2))
    (mixeds ++ square).foldLeft(linear + const)(_ + _)
  }
  def *(that: Interval)(implicit rnd: Rounding) = AffineScalarEnclosure(domain, normalizedDomain, constant * that, coefficients.mapValues(_ * that))
  def *(that: Double)(implicit rnd: Rounding): AffineScalarEnclosure = this * Interval(that)
  def *(that: Int)(implicit rnd: Rounding): AffineScalarEnclosure = this * Interval(that)

  /** Division of enclosures. */
  def /(that: Interval)(implicit rnd: Rounding) = AffineScalarEnclosure(domain, normalizedDomain, constant / that, coefficients.mapValues(_ / that))
  def /(that: Double)(implicit rnd: Rounding): AffineScalarEnclosure = this / Interval(that)
  def /(that: Int)(implicit rnd: Rounding): AffineScalarEnclosure = this / Interval(that)

  /**
   * An enclosure for the primitive function (w.r.t. "name") of this enclosure over the
   * domain, normalized to be 0 at the low end-point of the domain of "name".
   */
  def primitive(name: VarName)(implicit rnd: Rounding) = {
    require(domain.contains(name))
    val lo = domain(name).low
    val coeff = coefficients.getOrElse(name, Interval(0))
    val quadr = quadratic(name) * (coeff / 2)
    val prods = (coefficients - name).map { case (n, c) => mixed(name, n) * c }
    val nonlinear = quadr + prods.foldLeft(AffineScalarEnclosure(domain, Interval(0))) { case (res, f) => res + f }
    val linear = (AffineScalarEnclosure(domain, name) * constant) -
      (AffineScalarEnclosure(domain, Interval(0), coefficients - name) * lo)
    val nonconst = linear + nonlinear
    val cst = -lo * (constant + coeff * lo / 2)
    nonconst + cst
  }

  /** Returns an enclosure with the same affine interval function as the enclosure, defined over a sub-box of the domain. */
  //TODO Add property
  def restrictTo(subDomain: Box)(implicit rnd: Rounding): AffineScalarEnclosure = {
    require((domain contains subDomain) &&
      ((Box.normalize(domain) contains Box.normalize(subDomain))))
    AffineScalarEnclosure(subDomain, constant, coefficients)
  }

  private def quadratic(name: VarName)(implicit rnd: Rounding) = AffineScalarEnclosure.quadratic(domain, name)
  private def mixed(name1: VarName, name2: VarName)(implicit rnd: Rounding) = AffineScalarEnclosure.mixed(domain, name1, name2)

}

// TODO improve how the plotting is done
object AffineScalarEnclosure extends Plotter {

  /** Convenience method, normalizes the domain. */
  private[enclosure] def apply(domain: Box, constant: Interval, coefficients: Box): AffineScalarEnclosure =
    AffineScalarEnclosure(domain, Box.normalize(domain), constant, coefficients)

  /** Lifts a constant interval to a constant enclosure. */
  def apply(domain: Box, constant: Interval): AffineScalarEnclosure = {
    AffineScalarEnclosure(domain, constant, Box.empty)
  }

  /** Lifts a variable "name" in the domain to an identity function over the corresponding interval. */
  def apply(domain: Box, name: VarName)(implicit rnd: Rounding): AffineScalarEnclosure = {
    assert(domain contains name,
      "Projecting is only possible for variables in the domain.")
    /* Implementation note: The constant term needs to be domain(name).low 
     * because the internal representation is over the normalized domain. */
    AffineScalarEnclosure(domain, domain(name).low, Map(name -> Interval(1)))
  }

  /**
   * Degree reduction for "pure terms"
   * map x^2 from [a,b] to [-1,1] using x => 0.25*((b-a)*x+a+b)^2
   * map t^2 => 0.5*(T_2+1) and then T_2 => [-1,1] (or just t^2 => [0,1])
   * map t from [-1,1] to [a,b] using t => (2*t-a-b)/(b-a)
   *
   * AffineIntervalFunction enclosure over doms of the quadratic monomial in variable name
   */
  def quadratic(domain: Box, name: VarName)(implicit rnd: Rounding) = {
    require(domain.contains(name))
    val a = domain(name).low
    val b = domain(name).high
    val width = b - a
    val coeff = b + a
    // This corresponds to translating to [-1,1], representing in Chebyshev basis, 
    // collapsing undesired (quadratic) terms and translating back.
    val const = ((Interval(0) /\ (width * width)) - (coeff * coeff)) / Interval(4)
    AffineScalarEnclosure(domain, const, Box(name -> coeff))
  }

  /** Enclosure over the domain of the mixed monomial in variables name1 and name2 */
  def mixed(domain: Box, name1: VarName, name2: VarName)(implicit rnd: Rounding) = {
    require(domain.contains(name1) && domain.contains(name2) && name1 != name2)
    val a1 = domain(name1).low
    val b1 = domain(name1).high
    val a2 = domain(name2).low
    val b2 = domain(name2).high
    val width1 = (b1 - a1) / 2
    val coeff1 = (b1 + a1) / 2
    val width2 = (b2 - a2) / 2
    val coeff2 = (b2 + a2) / 2
    val const = (Interval(-1, 1) * width1 * width2) - (coeff1 * coeff2)
    AffineScalarEnclosure(domain, const, Box(name1 -> coeff2, name2 -> coeff1))
  }

  // TODO the plotting functionality should be moved to UnivariateAffineScalarEnclosure

  // FIXME: hard-coded plotting as function of t
  def plot(them: AffineScalarEnclosure*)(implicit rnd: Rounding): Unit =
    plot("Picard plotter")(them: _*)

  def plot(frametitle: String)(them: AffineScalarEnclosure*)(implicit rnd: Rounding) {
    createFrame(frametitle)
    for (it <- them) {
      val dom = it.domain("t")
      def low(t: Double) = it.low(Box("t" -> t)) match { case Interval(lo, _) => lo.doubleValue }
      def high(t: Double) = it.high(Box("t" -> t)) match { case Interval(hi, _) => hi.doubleValue }
      val (lo, hi) = dom match { case Interval(lo, hi) => (lo.doubleValue, hi.doubleValue) }
      addFunctionEnclosure(lo, hi, high, low, 0, "")
    }
  }

}
