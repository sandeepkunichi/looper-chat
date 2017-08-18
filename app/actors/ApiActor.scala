package actors

import actors.ApiActor.ApiEvent
import akka.actor.{Actor, ActorLogging, Props}
import data.MessageObjects.Message
import services.ApiService

/**
  * Created by Sandeep.K on 18-08-2017.
  */

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

