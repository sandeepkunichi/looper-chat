package controllers

import javax.inject.{Inject, Singleton}

import actors.MessageActor
import actors.MessageActor.MessageEvent
import akka.actor.ActorSystem
import akka.stream.Materializer
import data.MessageObjects.Message
import play.api.libs.json.Json
import play.api.libs.streams.ActorFlow
import play.api.libs.ws.WSClient
import play.api.mvc._
import services.{ApiService, BotService}
/**
  * Created by Sandeep.K on 18-08-2017.
  */

@Singleton
class ChatController @Inject() (implicit system: ActorSystem, materializer: Materializer, botService: BotService, apiService: ApiService) extends Controller {

  def index = Action { request =>
    Ok(views.html.index.render())
  }

  def sendMessage = Action { request =>
    system.actorSelection("akka://application/user/*/flowActor") ! MessageEvent(Json.parse(request.body.asJson.get.toString()).as[Message], botService, apiService)
    Ok("")
  }

  def chatSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => MessageActor.props(out))
  }
}
