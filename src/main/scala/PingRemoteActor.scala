
import Constants._
import akka.actor._
import com.typesafe.config.ConfigFactory

object PingRemoteActor extends App {

  class RemotePingActor extends Actor {
    def receive = {
      case INIT(ref) =>
        println("Started Ping Actor")
        ref ! ExecuteMethod()
        ref ! PONG
      case PING =>
        Thread.sleep(1000)
        println("Recieved PING")
        sender() ! PONG
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
      port = 2552
    }
 }
}
  """)

  implicit val actorSystem = ActorSystem("PingRemoteActorSystem", ConfigFactory.load(customConf))
  val pongRemoteActor = actorSystem.actorSelection("akka.tcp://PongRemoteActorSystem@127.0.0.1:2551/user/RemotePongActor")
  val pingActor = actorSystem.actorOf(Props[RemotePingActor], name = "RemotePingActor")
  pingActor ! INIT(pongRemoteActor)
}
