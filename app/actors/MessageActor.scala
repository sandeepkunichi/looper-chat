package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.hazelcast.core.IMap
import data.MessageObjects.{ChannelConnection, Message}
import services.{ApiService, BotService}

/**
  * Created by Sandeep.K on 18-08-2017.
  */
class MessageActor(messageActor: ActorRef,
                   name: String,
                   registry: IMap[String, Set[String]],
                   channelConnection: ChannelConnection)
  extends Actor with ActorLogging {

  val child = context.actorOf(Props(new ChatActor(self)), name)

  def receive = {
    case message: String => messageActor ! message
  }

  override def preStart = {
    if(registry.get(channelConnection.channel) == null)
      registry.set(channelConnection.channel, Set.empty)
    if(!registry.get(channelConnection.channel).contains(channelConnection.user)){
      registry.set(channelConnection.channel, registry.get(channelConnection.channel) + channelConnection.user)
    }
  }

  override def postStop = {
    registry.set(channelConnection.channel, registry.get(channelConnection.channel) - channelConnection.user)
  }

}

object MessageActor {
  def props(out: ActorRef, name: String, registry: IMap[String, Set[String]], channelConnection: ChannelConnection) =
    Props(new MessageActor(out, name, registry, channelConnection))
  case class MessageEvent(message: Message, botService: BotService, apiService: ApiService)
}
