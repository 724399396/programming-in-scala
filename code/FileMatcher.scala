object FileMatcher {
    private def filesHere = (new java.io.File(".")).listFiles
    def filesEnding(query: String) =
        fileMatching(_.endsWith(query))
            
    def filesContaining(query: String) = 
        fileMatching(_.contains(_))

    def filesRegex(query: String) =
        fileMatching(_.matches(_))

    def filesMatching(
            matcher: String => Boolean) = {
        for (file <- filesHere; if matcher(file.getName))
            yield file
}
