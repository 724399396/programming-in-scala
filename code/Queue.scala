trait Queue[T] {
    def head: T
    def tail: Queue[T]
    def append(x: T): Queue[T]
}


object Queue {
    // constructs a queue with initial elements 'xs'
    def apply[T](xs: T*): Queue[T] = new QueueImpl[T](xs.toList, Nil)    
    private class QueueImpl[T](
        private val leading: List[T],
        private val trailing: List[T]
    ) extends Queue[T] {
        private def mirror = 
        if (leading.isEmpty)
            new QueueImpl(trailing.reverse, Nil)
        else
            this
        def head = mirror.leading.head

        def tail = {
            val q = mirror
            new QueueImpl(q.leading.tail, q.trailing)
        }

        def append(x: T) = 
            new QueueImpl(leading, x :: trailing)
    }

}
