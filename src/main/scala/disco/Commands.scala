package disco

import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono

object Commands {
  type Command = MessageCreateEvent => Mono[Void]

  def ping (event: MessageCreateEvent): Mono[Void] =
    event.getMessage.getChannel
      .flatMap(_.createMessage("Pong!"))
      .`then`()

  val commands = Map[String, Command](
    "ping" -> ping
  )
}
