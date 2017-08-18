package actors

import actors.BotActor.BotMessageEvent
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import data.MessageObjects.Message
import services.BotService

/**
  * Created by Sandeep.K on 18-08-2017.
  */

import scala.concurrent.ExecutionContext.Implicits.global

class BotActor extends Actor with ActorLogging {
  def receive = {
    case BotMessageEvent(message, botService, messageActor) =>
      botService.askAgent(message).onComplete(response => {
        if(!response.get.text.isEmpty)
          messageActor ! BotMessageEvent(response.get, botService, messageActor)
      })
  }
}

object BotActor {
  def props = Props[BotActor]
  case class BotMessageEvent(message: Message, botService: BotService, messageActor: ActorRef)
}

