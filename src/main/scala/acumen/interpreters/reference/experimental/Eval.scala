package acumen
package interpreters
package reference
package experimental

import Interpreter._
import acumen.Errors._
import scala.collection.immutable.Set

/* a custom state+writer monad, inpired by the state monad of scalaz */

sealed trait Eval[+A] {
  import Eval._

  def apply(s: Store) : 
    (A /* result */, 
     Set[CId] /* dead (writer part) */, 
     Set[(CId,CId)] /* reparentings (writer part) */,
     Set[(CId,Dot,CValue)], /* discrete assignments */
     Set[(CId,Dot,Expr,Env)], /* continuous assignments (equations) */
     Set[(CId,Dot,Expr,Env)], /* ode assignments (differential equations) */
     Store /* current store (state part) */)

  def map[B](f: A => B) : Eval[B] = mkEval (
    apply(_) match { case (a, ids, rps, ass, eqs, odes, st) => (f(a), ids, rps, ass, eqs, odes, st) }
  )

  def foreach[B](f: A => Eval[B]) : Eval[B] = flatMap(f)

  def flatMap[B](f: A => Eval[B]) : Eval[B] = mkEval (
    apply(_) match { 
      case (a, ids, rps, ass, eqs, odes, st) => 
        val (a1, ids1, rps1, ass1, eqs1, odes1, st1) = f(a)(st)
        (a1, ids ++ ids1, rps ++ rps1, ass ++ ass1, eqs ++ eqs1, odes ++ odes1, st1)
    }
  )
  
  def >>[B](m: Eval[B]) = this flatMap (_ => m)
  def >>=[B](f: A => Eval[B]) : Eval[B] = flatMap(f)
  
  def filter(p: A => Boolean) : Eval[A] = mkEval (
    apply(_) match { case t@(a, _, _, _, _, _, _) =>
      if (p(a)) t else throw ShouldNeverHappen() 
    } 
  )

  def !  (s: Store) : A = apply(s)._1
  def ~> (s:Store) : Store = apply(s)._7

}

object Eval {

  def mkEval[A](f: Store => (A, Set[CId], Set[(CId,CId)], Set[(CId,Dot,CValue)], Set[(CId,Dot,Expr,Env)], Set[(CId,Dot,Expr,Env)], Store)) : Eval[A] = 
	new Eval[A] { def apply(s:Store) = f(s) }
 
  /* used to inject write operations of 'util.Canonical' into the monad */
  def promote(f: Store => Store) : Eval[Unit] =
    mkEval (s => ((), Set.empty, Set.empty, Set.empty, Set.empty, Set.empty, f(s)))

  def pure[A](x:A) : Eval[A] = mkEval(s => (x, Set.empty, Set.empty, Set.empty, Set.empty, Set.empty, s))
  def pass = pure(())
  
  def getStore : Eval[Store] = mkEval(s => (s, Set.empty, Set.empty, Set.empty, Set.empty, Set.empty, s))
  def modifyStore(f:Store => Store) : Eval[Unit] = 
    mkEval(s => ((), Set.empty, Set.empty, Set.empty, Set.empty, Set.empty, f(s)))

  def logCId(id:CId) : Eval[Unit] = 
    mkEval (s => ((), Set(id), Set.empty, Set.empty, Set.empty, Set.empty, s))

  def logReparent(o:CId, parent:CId) : Eval[Unit] =
    mkEval(s => ((), Set.empty, Set((o,parent)), Set.empty, Set.empty, Set.empty, s))
    
  def logAssign(o: CId, d: Dot, v:CValue) : Eval[Unit] =
    mkEval(s => ((), Set.empty, Set.empty, Set((o,d,v)), Set.empty, Set.empty, s))

  def logEquation(o: CId, d: Dot, r: Expr, e: Env) : Eval[Unit] =
    mkEval(s => ((), Set.empty, Set.empty, Set.empty, Set((o,d,r,e)), Set.empty, s))

  def logODE(o: CId, d: Dot, r: Expr, e: Env) : Eval[Unit] =
    mkEval(s => ((), Set.empty, Set.empty, Set.empty, Set.empty, Set((o,d,r,e)), s))

  /* Apply f to the store and wrap it in an Eval */
  def asks[A](f : Store => A) : Eval[A] = 
    getStore >>= ((s:Store) => pure(f(s)))
  
  def sequence[A](es:List[Eval[A]]) : Eval[List[A]] = es match {
    case Nil     => pure(Nil)
    case (e::es) => for (x  <- e; xs <- sequence(es)) yield x::xs 
  }
  def sequence_[A](es:List[Eval[A]]) : Eval[Unit] =
    es match {
      case Nil     => pure(())
      case (e::es) => e >> sequence_(es)  
    }
  
  def mapM[A,B](f:A => Eval[B], xs:List[A]) : Eval[List[B]] = 
    sequence(xs map f)
  def mapM_[A,B](f:A => Eval[B], xs:List[A]) : Eval[Unit] = 
    sequence_(xs map f)

} 
