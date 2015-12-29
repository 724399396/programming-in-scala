package com.wei.li.scells

trait Formula

case class Coord(val row: Int, val column: Int) 
        extends Formula {
    override def toString = (column + 'A').toChar.toString + row
}

case class Range(val c1: Coord, val c2: Coord)
        extends Formula {
    override def toString = c1.toString + ":" + c2.toString
}

case class Number(val value: Double)
        extends Formula {
    override def toString = value.toString
}

case class Textual(val value: String)
        extends Formula {
    override def toString = value
}

case class Application(func: String, args: List[Formula])
        extends Formula {
    override def toString = func + args.mkString("(",",",")")
}

object Empty extends Textual("")