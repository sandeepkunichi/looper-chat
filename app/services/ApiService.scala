package services

import javax.inject.Inject

import data.MessageObjects
import data.MessageObjects.Message
import play.Logger
import play.api.libs.ws.WSClient

/**
  * Created by Sandeep.K on 18-08-2017.
  */
class ApiService @Inject() (wsClient: WSClient) {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  def postMessage(message: Message): Unit = {
    val data = MessageObjects.messageFormat.writes(message)
    wsClient
      .url("http://192.168.20.154:9001/message")
      .post(data)
      .map { response => Logger.info("response: " + response.body) }.onComplete(x => x.get)
  }
}
