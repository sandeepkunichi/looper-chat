package controllers

import java.util.function.Consumer
import javax.inject.{Inject, Singleton}

import actors.MessageActor
import actors.MessageActor.MessageEvent
import akka.actor.{ActorSystem, Props}
import akka.stream.Materializer
import com.hazelcast.client.HazelcastClient
import com.hazelcast.client.config.ClientConfig
import data.MessageObjects.{ChannelConnection, Connections, Message}
import play.api.libs.json.Json
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import services.{ApiService, BotService}

/**
  * Created by Sandeep.K on 18-08-2017.
  */

@Singleton
class ChatController @Inject() (implicit system: ActorSystem,
                                materializer: Materializer,
                                botService: BotService,
                                apiService: ApiService) extends Controller {

  val clientConfig = new ClientConfig()
  clientConfig.getNetworkConfig.addAddress("192.168.10.94:5701")
  val hazelcastClient = HazelcastClient.newHazelcastClient(new ClientConfig())
  val actorRegistry = hazelcastClient.getMap[String, Set[String]]("looper-actors")

  def index = Action { request =>
    Ok(views.html.index.render())
  }

  def sendMessage = Action { request =>
    actorRegistry.get("random").foreach(p => {
      system.actorSelection("akka://looper-actor-system-1/user/*/flowActor/" + p) !
        MessageEvent(Json.parse(request.body.asJson.get.toString()).as[Message], botService, apiService)
      system.actorSelection("akka://looper-actor-system-2/user/*/flowActor/" + p) !
        MessageEvent(Json.parse(request.body.asJson.get.toString()).as[Message], botService, apiService)
      system.actorSelection("akka://looper-actor-system-3/user/*/flowActor/" + p) !
        MessageEvent(Json.parse(request.body.asJson.get.toString()).as[Message], botService, apiService)
    })
    Ok("")
  }

  def chatSocket(channel: String, user: String) = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => {
      Props(new MessageActor(out, user, actorRegistry, ChannelConnection(channel, user)))
    })
  }

  def getConnectedParties = Action { request =>
    var connections: Set[Connections] = Set.empty

    actorRegistry.keySet().forEach(new Consumer[String] {
      override def accept(channel: String): Unit = {
        connections = connections + new Connections(channel, actorRegistry.get(channel))
      }
    })

    Ok(views.html.connections.render(Json.toJson(connections).toString()))
  }
}
