
import Constants._
import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object PongRemoteActor extends App {

  class RemotePongActor extends Actor {
    def receive = {
      case KEEPALIVE =>
        println("KeepAlive")
      case e: ExecuteMethodBase =>
        println("ExecuteMethod")
        e.executeMethod()
      case PONG =>
        println("Recieved PONG")
        Thread.sleep(1000)
        println(sender())
        sender() ! PING
      case _ =>
        println("Invalid Message")
    }
  }

  val customConf = ConfigFactory.parseString("""
  akka {
  actor {
    provider = remote
  }
  akka.actor.warn-about-java-serializer-usage = off
  remote {
    enabled-transports = ["akka.remote.netty.tcp"]
    netty.tcp {
      hostname = "127.0.0.1"
      port = 2551
    }
 }
}
  """)
  implicit val system = ActorSystem("PongRemoteActorSystem", ConfigFactory.load(customConf))
  val remotePongActor = system.actorOf(Props[RemotePongActor], name = "RemotePongActor")
  remotePongActor ! KEEPALIVE
}
