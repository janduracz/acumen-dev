package acumen.interpreters.enclosure

import Interval._
import Types._

object UnaryRelationName extends Enumeration {
  type UnaryRelationName = Value
  val Positive, NonNegative, EqualToZero, NonPositive, Negative = Value
}
import UnaryRelationName._

/**
 * Type used to represent relations used to define predicates.
 *
 * Implementation note: we are initially aiming to support the benchmarks from
 * the paper "Enclosing Hybrid Behavior". These benchmark hybrid systems have
 * limited range of predicates appearing as event guards and mode invariants.
 * In particular all these predicates are of the form of an inequality between
 * 0 and a variable. Handling of more complex expressions in inequalities will
 * require constraint solving to deduce the support of predicates used in the
 * solveVt algorithm.
 */
abstract class Relation {
  /**
   * Evaluate the predicate by taking the variables to range over the intervals
   * of the box x.
   *
   * Note that the result will be a set of Boolean values rather than a single
   * one as the predicate is evaluated over intervals, i.e. sets of reals.
   */
  def apply(x: Box)(implicit rnd: Rounding): Set[Boolean] = this match {
    case UnaryRelation(relname, e) => relname match {
      case Positive => {
        val value = e(x)
        if (value greaterThan 0) Set(true)
        else if (value lessThanOrEqualTo 0) Set(false)
        else Set(true, false)
      }
      case NonNegative => {
        val value = e(x)
        if (value greaterThanOrEqualTo 0) Set(true)
        else if (value lessThan 0) Set(false)
        else Set(true, false)
      }
      case EqualToZero => {
        val value = e(x)
        if (value equalTo 0) Set(true)
        else if (value contains 0) Set(true, false)
        else Set(false)
      }
      case NonPositive => {
        val value = e(x)
        if (value lessThanOrEqualTo 0) Set(true)
        else if (value greaterThan 0) Set(false)
        else Set(true, false)
      }
      case Negative => {
        val value = e(x)
        if (value lessThan 0) Set(true)
        else if (value greaterThanOrEqualTo 0) Set(false)
        else Set(true, false)
      }
      case _ => sys.error("Relation.eval: " + this.toString)
    }
  }

  // TODO do something about the code duplication in these instances!
  /**
   * Evaluate the relation by composing with the enclosure and taking the variables 
   * to range over the intervals of the box x.
   *
   * Note that the result will be a set of Boolean values rather than a single one
   * as the relation is evaluated over the domains of variables, i.e. sets of reals.
   */
  def apply(x: UnivariateAffineEnclosure)(implicit rnd: Rounding): Set[Boolean] = this match {
    case UnaryRelation(relname, e) => relname match {
      case Positive => {
        val value = e(x).range
        if (value greaterThan 0) Set(true)
        else if (value lessThanOrEqualTo 0) Set(false)
        else Set(true, false)
      }
      case NonNegative => {
        val value = e(x).range
        if (value greaterThanOrEqualTo 0) Set(true)
        else if (value lessThan 0) Set(false)
        else Set(true, false)
      }
      case EqualToZero => {
        val value = e(x).range
        if (value equalTo 0) Set(true)
        else if (value contains 0) Set(true, false)
        else Set(false)
      }
      case NonPositive => {
        val value = e(x).range
        if (value lessThanOrEqualTo 0) Set(true)
        else if (value greaterThan 0) Set(false)
        else Set(true, false)
      }
      case Negative => {
        val value = e(x).range
        if (value lessThan 0) Set(true)
        else if (value greaterThanOrEqualTo 0) Set(false)
        else Set(true, false)
      }
      case _ => sys.error("Relation.eval: " + this.toString)
    }
  }

  /** A conservative approximation of the intersection of x with the support of r. */
  def support(x: Box)(implicit rnd: Rounding): Box = this match {
    case r @ UnaryRelation(relname, Variable(name)) => relname match {
      case Positive => {
        if (x(name) lessThanOrEqualTo 0) sys.error("Relation.support: Positive: empty intersection")
        else x - name + (name -> max(0, x(name).low) /\ x(name).high)
      }
      case NonNegative => {
        if (x(name) lessThan 0) sys.error("Relation.support: NonNegative: empty intersection with " + x(name))
        else x - name + (name -> max(0, x(name).low) /\ x(name).high)
      }
      case EqualToZero => {
        if (!(x(name) contains 0)) sys.error("Relation.support: EqualToZero: empty intersection")
        else x - name + (name -> Interval(0))
      }
      case NonPositive => {
        if (x(name) greaterThan 0) sys.error("Relation.support: NonPositive: empty intersection")
        else x - name + (name -> x(name).low /\ min(0, x(name).high))
      }
      case Negative => {
        if (x(name) greaterThanOrEqualTo 0) sys.error("Relation.support: Negative: empty intersection")
        else x - name + (name -> x(name).low /\ min(0, x(name).high))
      }
    }
    case r @ UnaryRelation(relname, Negate(e)) => relname match {
      case Positive => Relation.negative(e).support(x)
      case NonNegative => Relation.nonPositive(e).support(x)
      case EqualToZero => Relation.equalToZero(e).support(x)
      case NonPositive => Relation.nonNegative(e).support(x)
      case Negative => Relation.positive(e).support(x)
    }
    case r @ UnaryRelation(relname, Divide(e, Constant(c))) =>
      if (c greaterThan 0) UnaryRelation(relname, e).support(x)
      else if (c lessThan 0) UnaryRelation(relname, Negate(e)).support(x)
      else sys.error("Relation.support: division by 0")
  }

  /** Returns the set of variable names which occur in the relation. */
  def varNames: Set[VarName] = this match {
    case UnaryRelation(_, expression) => expression.varNames
  }

  /**
   * Tests if the relation is one of the reflexive inequalities, i.e.
   * of the form 0 <= e or e <= 0 for some expression e.
   */
  def isNonStrict: Boolean = this match {
    case UnaryRelation(n, _) => n match {
      case NonPositive => true
      case NonNegative => true
      case _ => false
    }
  }

}
object Relation {
  def positive(that: Expression) = UnaryRelation(Positive, that)
  def nonNegative(that: Expression) = UnaryRelation(NonNegative, that)
  def equalToZero(that: Expression) = UnaryRelation(EqualToZero, that)
  def nonPositive(that: Expression) = UnaryRelation(NonPositive, that)
  def negative(that: Expression) = UnaryRelation(Negative, that)
}

case class UnaryRelation(name: UnaryRelationName, expression: Expression) extends Relation {
  override def toString = name match {
    case Positive => expression.toString + " > 0"
    case NonPositive => expression.toString + " <= 0"
    case EqualToZero => expression.toString + " = 0"
    case NonNegative => expression.toString + " >= 0"
    case Negative => expression.toString + " < 0"
  }
}

