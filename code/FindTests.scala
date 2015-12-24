object FindTests extends App {
     for {
       method <- Tests.getClass.getMethods
       if method.getName.startsWith("test")
       if method.getAnnotation(classOf[Ignore])
     } {
       println("found a test method: " + method)
     }
}
