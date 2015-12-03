import org.scalatest.prop.FunSuite
import org.scalacheck.Prop._
import Element.elem

class ElementSuite extends FunSuite {
    test("elem result should have passed width", (w: Int) =>
        w > 0 ==> (elem('x', w, 3).width == w)
    )

    test("elem result should have passed height", (w: Int) =>
        h > 0 ==> (elem('x', 2, h).height == h)
    )
}
