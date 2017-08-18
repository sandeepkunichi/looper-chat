package services

import java.util.UUID
import javax.inject.Inject

import data.MessageObjects.Message
import play.api.libs.json.JsValue
import play.api.libs.ws._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
  * Created by Sandeep.K on 18-08-2017.
  */
class BotService @Inject() (wsClient: WSClient){

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  def askAgent(message: Message): Future[Message] = {
    val futureResult: Future[Message] = wsClient
      .url("https://api.api.ai/v1/query")
      .withHeaders("Authorization" -> "Bearer 4186e54dcd714aa0a7e4ae784ed90ad4")
      .withQueryString(("v","20150910"),
        ("query", message.text),
        ("lang", "en"),
        ("sessionId", UUID.randomUUID().toString),
        ("timezone", "Asia/Calcutta"))
      .get()
      .map {
        response => {
          val botMessage = response.json.as[JsValue] \ "result" \ "fulfillment" \ "speech"
          new Message(1, botMessage.get.as[String], message.userId, message.channelId)
        }
      }
    futureResult
  }


}
