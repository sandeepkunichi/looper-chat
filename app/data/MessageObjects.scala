package data

import play.api.libs.json._

/**
  * Created by Sandeep.K on 18-08-2017.
  */
object MessageObjects {

  case class Message (id: Long, text: String, userId: Long, username: String, channelId: Long)
  case class BotMessage (text: String, userId: Long, channelId: Long)
  case class User (id: Long, username: String)
  case class Channel (id: Long, name: String)
  case class Connections (channel: String, users: Set[String])
  case class ChannelConnection (channel: String, user: String)

  implicit val messageFormat = new Format[Message] {
    override def reads(json: JsValue): JsResult[Message] = {
      JsSuccess(new Message(
        (json \ "id").as[Long],
        (json \ "text").as[String],
        (json \ "userId").as[Long],
        (json \ "username").as[String],
        (json \ "channelId").as[Long]
      ))
    }

    override def writes(message: Message): JsValue = {
      Json.obj(
        "id" -> message.id,
        "text" ->  message.text,
        "userId" -> message.userId,
        "username" -> message.username,
        "channelId" -> message.channelId
      )
    }
  }

  implicit val userFormat = new Format[User] {
    override def reads(json: JsValue): JsResult[User] = {
      JsSuccess(new User(
        (json \ "id").as[Long],
        (json \ "username").as[String]
      ))
    }

    override def writes(user: User): JsValue = {
      Json.obj(
        "id" -> user.id,
        "username" ->  user.username
      )
    }
  }

  implicit val channelFormat = new Format[Channel] {
    override def reads(json: JsValue): JsResult[Channel] = {
      JsSuccess(new Channel(
        (json \ "id").as[Long],
        (json \ "name").as[String]
      ))
    }

    override def writes(channel: Channel): JsValue = {
      Json.obj(
        "id" -> channel.id,
        "name" ->  channel.name
      )
    }
  }

  implicit val botMessageFormat = new Format[BotMessage] {
    override def reads(json: JsValue): JsResult[BotMessage] = {
      JsSuccess(new BotMessage(
        (json \ "text").as[String],
        (json \ "user").as[Long],
        (json \ "channel").as[Long]
      ))
    }

    override def writes(message: BotMessage): JsValue = {
      Json.obj(
        "text" ->  message.text,
        "userId" -> message.userId,
        "channelId" -> message.channelId
      )
    }
  }

  implicit val connectionFormat = new Format[Connections] {
    override def reads(json: JsValue): JsResult[Connections] = {
      JsSuccess(new Connections(
        (json \ "channel").as[String],
        (json \ "users").as[Set[String]]
      ))
    }

    override def writes(connections: Connections): JsValue = {
      Json.obj(
        "channel" -> connections.channel,
        "users" ->  connections.users
      )
    }
  }
}
