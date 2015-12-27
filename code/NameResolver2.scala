import scala.actors.Actor
import scala.actors.Actor._
import java.net.{InetAddress, UnknownHostException}

case class LookupIP(name: String, respondTo: Actor)
case class LookupResult (
    name: String,
    address: Option[InetAddress]
) 

object NameResolver2 extends Actor {
    def act() {
        loop {
            react {
              case LookupIP(name, actor) =>
                actor ! LookupResult(name, getIp(name))
            }
        }
    }

    def getIp(name: String): Option[InetAddress] = {
        try {
            Some(InetAddress.getByName(name))
        } catch {
          case _:UnknownHostException => None
        }
    }
}
