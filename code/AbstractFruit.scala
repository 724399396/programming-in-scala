abstract class Fruit {
    val v: String // 'v' for value
    val m: String // 'm' for method
}

abstract class Apple extends Fruit {
    val v: String
    val m: String // OK to overrride a 'def' with a 'val
}

abstract class BadApple extends Fruit {
    def v: String // ERROR: cannot override a 'val' with a 'def'
    def m: String
}
