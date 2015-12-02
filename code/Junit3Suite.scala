import org.scalatest.junit.JUnit3Suite
import Element.elem

class ELementSuite extends JUnit3Suite {
    def testUniformElement() {
        val ele = elem('x', 2, 3)
        assert(ele.width === 2)
        expect(3) { ele.height }
        intercept(classOf[IllegalArgumentException]) {
            elem('x', -2, 3)
        }
    }
}

import org.testng.annotations.Test
import org.testng.Assert.assertEquals
import Element.elem

class ElementTests {
    @Test def verifyUniformElement() {
        val ele = elem('x', 2, 3)
        assertEquals(ele.width, 2)
        assertEquals(ele.height, 3)
    }
    @Test {
        val expectedExceptions =
            Array(classOf[IllegalArgumentException])
    }
    def elemShouldThrowIAE() { elem('x', -2, 3) }
}

import org.scalatest.testng.TestNGSuite
import org.testng.annotations.Test
import Element.elem

class ElementSuite extends TestNGSuite {
    @Test def verifyUniformElement() {
        val ele = elem('x', 2, 3)
        assert(ele.width === 2)
        expect(3) { ele.height }
        intercept(classOf[IllegalArgumentException]) {
            elem('x', -2, 3)
        }
    }
}
