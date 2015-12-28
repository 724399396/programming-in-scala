type Parser[T] = Input => ParseResult[T]

type Input = Reader[Elem]

sealed abstract class ParseResult[+T]
case class Success[T](result: T, in: Input) extends ParseResult[T]
case class Failure(msg: String, in: Input) extends ParseResult[Nothing]

abstract class Parser[+T] extends (Input => ParseResult[T]) {
 p =>
    type Elem

    def apply(in: Input): ParseResult[T]

    def elem(kind: String, p: Elem => Boolean) =
        new Parser[Elem] {
            def apply(in: Input) =
                if (p(in.first)) Success(in.first, in.rest)
                else Failure(kind + " expected", in)
        }

    def ~ [U](q: => Parse[U]) = new Parse[T~U] {
        def apply(in: Input) = p(in) match {
            case Success(x, in1) =>
                q(in1) match {
                    case Success(y, in2) => Success(new ~(x,y), in2)
                    case failure => failure
                }
            case failure => failure
        }
    }
    
    def <~ [U](q: => Parser[U]): Parser[T] =
        (p~q) ^^ { case x~y => x }
    def ~> [U](q: => Parser[U]): Parser[U] =
        (p~q) ^^ { case x~y => y }

    def | (q: => Parser[T]) = new Parser[T] {
        def apply(in: Input) = p(in) match {
            case s1 @ Success(_,_) => s1
            case failure => q(in)
        }
    }

    def ^^ [U](f: T => U): Parser[U] = new Parser[U] {
        def apply(in: Input) = p(in) match {
            case Success(x, in1) => Success(f(x), in1)
            case failure => failure
        }
    }

    def success[T](v: T) = new Parser[T] {
        def apply(in: Input) = Success(v, in)
    }

    def failure(msg: String) = new Parser[Nothing] {
        def apply(in: Input) = Failure(msg, in)
    }

    def opt[T](p: => Parser[T]): Parser[Option[T]] = {
        p ^^ Some(_)
        | Success(None)
    }

    def rep[T](p: Parser[T]): Parser[List[T]] = (
        p~rep(p) ^^ { case x~xs => x :: xs }
        | Success(List())
    )

    def repsep[T, U](p: Parser[T], q: Parser[U]): Parser[List[T]] = (
        p~rep(q~> p) { case r-rs => r :: rs }
        | success(List())
    )
}