import org.scalatest.Spec

class ElementSpec extends Spec {
    "A UniformElement" -- {
        "should have a width equals to the passed value" - {
            val ele = elem('x', 2, 3)
            assert(ele.width === 2)
        }
        "should have a height equal to the passed value" - {
            val ele = elem('x', 2, 3)
            assert(ele.height === 3)
        }
        "should throw an IAE if passed a nagative width" - {
            intercept(classOf[IllegalArgumentException]) {
                elem('x', -2, 3)
            }
        }
    }
}
