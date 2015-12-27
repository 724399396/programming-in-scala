import java.io.FileReader

object ParseJSON extends JSON {
    def main(args: Array[String]) {
        val reader = new FileReader(args(0))
        println(parseAll(value,reader))
    }
}
