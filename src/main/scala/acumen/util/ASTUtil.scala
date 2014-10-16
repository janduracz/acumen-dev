package acumen
package util

import Errors.PositionalAcumenError

object ASTUtil {

  def exprSubParts(e: Expr) : List[Expr] = e match {
    case Lit(v) => Nil
    case Var(v) => Nil
    case Op(name, es) => es
    case Dot(a,b) => List(a)
    case ExprVector(l) => l
    case Sum(s, i, col, cond) => List(s,col,cond)
    case TypeOf(v) => Nil
    case ExprInterval(lo, hi) => List(lo,hi)
    case ExprIntervalM(mid, pm) => List(mid,pm)
  }

  def exprSubParts(i: InitRhs) : List[Expr] = i match {
    case NewRhs(_, fields) => fields
    case ExprRhs(e) => List(e)
  }
  
  def op(o: String, ps: Expr*): Expr = Op(Name(o,0), ps.toList)

  /** Replaces all occurrences of d in h for e. */
  def substitute(d: Dot, e: Expr, h: Expr): Expr = {
    def sub(h: Expr): Expr = substitute(d, e, h)
    h match {
      case Lit(v)                 => h
      case Var(v)                 => sys.error("Unqualified variables are nor supported.")
      case Op(name, es)           => Op(name, es map sub)
      case dot: Dot               => if (d == dot) e else dot
      case Index(a, idx)          => Index(sub(a), sub(idx))
      case ExprVector(l)          => ExprVector(l map sub)
      case Sum(s, i, col, cond)   => Sum(sub(s), i, sub(col), sub(cond))
      case TypeOf(v)              => h
      case ExprInterval(lo, hi)   => ExprInterval(sub(lo), sub(hi))
      case ExprIntervalM(mid, pm) => ExprIntervalM(sub(mid), sub(pm))
      case ExprLet(bindings, e2)  => ExprLet(bindings.map { case (n, e) => (n, sub(e)) }, sub(e2))
    }
  }.setPos(h.pos)

  /** Returns all variables that occur in e. */
  def dots(e: Expr): List[Dot] = e match {
    case d: Dot                 => d :: Nil
    case Op(_, es)              => es flatMap dots
    case Index(a, idx)          => dots(a) ::: dots(idx)
    case ExprVector(l)          => l flatMap dots
    case Sum(s, i, col, cond)   => dots(s) ::: dots(col) ::: dots(cond)
    case ExprInterval(lo, hi)   => dots(lo) ::: dots(hi)
    case ExprIntervalM(mid, pm) => dots(mid) ::: dots(pm)
    case ExprLet(bindings, e2)  => bindings.flatMap(b => dots(b._2)) ::: dots(e2)
    case _                      => Nil
  }

  /** Allow hypotheses only at top level of classes. */
  def checkNestedHypotheses(prog: Prog): Unit = {
    def disallowHypotheses(a: Action, atTopLevel: Boolean): Unit = a match {
      case IfThenElse(_,t,e) => for (s <- t ::: e) disallowHypotheses(s, false) 
      case ForEach(_,_,b) => for (s <- b) disallowHypotheses(s, false)
      case Switch(_,cs) => for (c <- cs; s <- c.rhs) disallowHypotheses(s, false)
      case Hypothesis(s,p) if !atTopLevel => 
        throw new PositionalAcumenError{ def mesg = "Hypothesis statements are only allowed at the top level of classes." }.setPos(p.pos)
      case _ =>
    }
    for (cd <- prog.defs; a <- cd.body) disallowHypotheses(a, true)
  }

}

