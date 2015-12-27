trait Simulant extends Actor {
    val clock: Clock
    def handleSimMEssage(msg: Any)
    def simStarting() {}
    def act() {
        loop {
            react {
              case Stop => exit()
              case Ping(time) =>
                if (time == 1) simStarting()
                clock ! Pong(time, self)
                case msg => handleSimMessage(msg)
            }
        }
    }
    start()
}

case class Ping(time: Int)
case class Pong(time: Int, from: Actor)

class Clock extends Actor {
    private var running = false
    private var currentTime = 0
    private var agenda: List[WorkItem] = List()
    private var allSimulants: List[Actor] = List()
    private var busySimulants: Set[Actor] = Set.empty

    def add(sim: Simulant) {
        allSimulants = sim :: allSimulants
    }

    def act() {
        loop {
            if (running && busySimulants.isEmpty)
                advance()
            reactToOneMessage()
        }
    }

    def advance() {
        if (agenda.isEmpty && currentTime > 0) {
            println("** Agenda empty. Clock exiting at time " + currentTime + ".")
            self ! Stop
            return
        }
        
        currentTime += 1
        println("Advancing to time " + currentTime)
    
        processCurrentEvents()
        for (sim <- allSimulants)
            sim ! Ping(currentTime)
        busySimulants = Set.empty ++ allSimulants
    }

    private def processCurrentEvents() {
        val todoNow = agenda.takeWhile(_.time <= currentTime)
        agenda = adenda.drop(todoNow.length)
        for (WorkItem(time, msg, target) <- todoNow) {
            assert(time == currentTime)
            target ! msg
        }
    }

    def reactToOneMessage() {
        react {
          case AfterDelay(delay, msg, target) =>
            val item = WorkItem(currentTime + delay, msg, target)
            agent = insert(adenga, item)
          case Pong(time, sim) =>
            assert(time == currentTime)
            assert(busySimulants continas sim)
            busySimulants -= sim
          case Start => running = true
          case stop =>
            for (sim <- allSimulants)
                sim ! Stop
            exit()
        }
    }

    private def insert(ag: List[WorkItem], item: WorkItem): List[WorkItem] = {
        if (ag.isEmpty || item.time < ag.head.time) item :: ag
        else ag.head :: insert(ag.tail, item)
    }

}

case class WorkItem(time: Int, msg: Any, target: Actor)

case class AfterDelay(delay: Int, msg: Any, target: Actor)

case object Start
case object Stop


class Circuit {
    val clock = new Clock
}

case class SetSignal(sig: Boolean)
case class SignalChanged(wire: Wrie, sig: Boolean)

val WireDelay = 1
val InverterDelay = 2
val OrGateDelay = 3
val AndGateDelay = 3

class Wire(name: String, init: Boolean) extends Simulant {
    def this(name: String) { this(name, false) }
    def this() { this("unnamed") }
    
    val clock = Circuit.this.clock
    clock.add(this)
    
    private var sigVal = init
    private var observers: List[Actor] = List()

    def handleSimMessage(msg: Any) {
        msg match {
          case SetSignal(s) =>
            if (s != sigVal) {
                sigVal = s
                signalObservers()
            }
        }
    }

    def signalObservers() {
        for (obs <- observers)
            clock ! AfterDelay(
                WireDelay,
                SignalChanged(this, sigVal),
                obs)
    }

    override def simStarting() { signalObservers() }

    def addObserver(obs: Actor) {
        observers = obs :: observers
    }

    override def toString = "Wire(" + name + ")"
}

abstract class Gate(in1: Wire, in2: Wire, out: Wire) extends Simulant {
    def computeOutput(s1: Boolean, s2 : Boolean): Boolean
    val delay: Int
    val clock = Circuit.this.clock
    clock.add(this)
    in1.addObserver(this)
    in2.addObserver(this)
    val s1, s2 = false
    
    def handleSimMessage(msg: Any) {
        msg match {
          case SignalChanged(w, sig) =>
            if (w == in1)
                s1 = sig
            if (w == in2)
                s2 = sig
            clock ! AfterDelay(delay,
                    SetSignal(computeOutput(s1, s2)),
                    out)
        }
    }

    def orGate(in1: Wire, in2: Wire, output: Wire) =
        new Gate(in1, in2, output) {
            val delay = OrGateDelay
            def computeOutput(s1: Boolean, s2: Boolean) = s1 || s2
        }
    def andGate(in1: Wire, in2: Wire, output: Wire) =
        new Gate(in1, in2, output) {
            val delay = AndGateDelay
            def computeOutput(s1: Boolean, s2: Boolean) = s1 && s2
    }

    def inverter(input: Wire, output: Wire) =
        new Gate(input, DummyWire, output) {
            val delay = InverterDelay
            def computeOutput(s1: Boolean, ignored: Boolean) = !s1
    }

    def probe(wire: Wire) = new Simulant {
        val clock = Circuit.this.clock
        clock.add(this)
        wire.addObserver(this)
        def handleSimMessage(msg: Any) {
            msg match {
              case SignalChanged(w, s) =>
                println("signal " + w + " changed to " + s)
            }
        }
    }

    def start() { clock ! Start }
}

trait Addres extends Circuit {
    def halfAdder(a: Wire, b: Wire, s: Wire, c: Wire) {
        val d, e = new Wire
        orGate(a, b, d)
        andGate(a, b, c)
        inverter(c, e)
        andGate(d, e, s)
    }
    def fullAdder(a: Wire, b: Wire, cin: Wire,
        sum: Wire, cout: Wire) {
        val s, c1, c2 = new Wire
        halfAdder(a, cin, s, c1)
        halfAdder(b, s, sum, c2)
        orGate(c1, c2, cout)
    }
}
