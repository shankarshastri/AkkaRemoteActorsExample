import akka.actor.{ActorRef, ActorSelection}

object Constants {

  trait ExecuteMethodBase{
    def executeMethod(): Unit
  }
  case class ExecuteMethod() extends ExecuteMethodBase {
    override def executeMethod(): Unit = {
      println("Printing Inside Remote Actor")
    }
  }
  case object PING
  case object PONG
  case object KEEPALIVE
  case class INIT(pongActor: ActorSelection)
}
