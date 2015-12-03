sealed abstract class Expr
case class Var(name: String) extends Expr
case class Number(num: Double) extends Expr
case class UnOp(operator: String, arg: Expr) extends Expr
case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

def simplifyTop(expr: Expr): Expr = expr match {
  case UnOp("-", UnOp("-", e)) => e
  case BinOp("+", e, Number(0)) => e
  case BInOp("*", e, Number(1)) => e
  case _ => expr
}

def tupleDemo(expr: Any) =
    expr match {
      case (a, b, c) => println("matched " + a + b + c)
      case _ =>
    }

def generalSize(x: Any) = x match {
  case s: String => s.length
  case m: Map[_,_] => m.size
  case _ => -1
}

def simplifyAll(expr: Expr): Expr = expr match {
  case UnOp("-", UnOp("-", e)) =>
    simplifyAll(e)
  case BinOp("+", e, Number(0)) =>
    simplifyAll(e)
  case BinOp("*", e, Number(1)) =>
    simplifyAll(e)
  case UnOp(op, e) =>
    UnOp(op, simplifyAll(e))
  case BinOp(op, l, r) =>
    BinOp(op, simpilyAll(l), simplifyAll(r))
  case _ => expr
}
