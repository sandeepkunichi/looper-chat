package actors


import actors.ApiActor.ApiEvent
import actors.BotActor.BotMessageEvent
import actors.MessageActor.MessageEvent
import akka.actor.{Actor, ActorLogging, ActorRef, Address, AddressFromURIString, Props}
import akka.remote.routing.RemoteRouterConfig
import akka.routing.RoundRobinPool
import data.MessageObjects.Message
import play.api.libs.json.Json
import services.{ApiService, BotService}

/**
  * Created by Sandeep.K on 18-08-2017.
  */
class MessageActor(out: ActorRef) extends Actor with ActorLogging {

  private val botRouterAddresses: Array[Address] = Array(new Address("akka.tcp", "localhost", "127.0.0.1", 9001), AddressFromURIString.parse("akka.tcp://localhost@127.0.0.1:9001"))
  private val botRouter: ActorRef = context.actorOf(new RemoteRouterConfig(new RoundRobinPool(5), botRouterAddresses).props(Props.create(classOf[BotActor])))
  private val apiRouterAddresses: Array[Address] = Array(new Address("akka.tcp", "localhost", "127.0.0.1", 9002), AddressFromURIString.parse("akka.tcp://localhost@127.0.0.1:9002"))
  private val apiRouter: ActorRef = context.actorOf(new RemoteRouterConfig(new RoundRobinPool(5), apiRouterAddresses).props(Props.create(classOf[ApiActor])))

  def receive = {
    case MessageEvent(message, botService, apiService) =>
      botRouter ! BotMessageEvent(message, botService, self)
      out ! Json.toJson(message).toString()
      apiRouter ! ApiEvent(message, apiService)
    case BotMessageEvent(message, botService, self) => out ! Json.toJson(message).toString()
  }

}

object MessageActor {
  def props(out: ActorRef) = Props(new MessageActor(out))
  case class MessageEvent(message: Message, botService: BotService, apiService: ApiService)
}
