package actors

import actors.ApiActor.ApiEvent
import actors.BotActor.BotMessageEvent
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import data.MessageObjects.Message
import play.Logger
import services.{ApiService, BotService}

/**
  * Created by Sandeep.K on 18-08-2017.
  */

import scala.concurrent.ExecutionContext.Implicits.global

class ApiActor extends Actor with ActorLogging {
  def receive = {
    case ApiEvent(message, apiService) =>
      apiService.postMessage(message)
  }
}

object ApiActor {
  def props = Props[ApiActor]
  case class ApiEvent(message: Message, apiService: ApiService)
}

