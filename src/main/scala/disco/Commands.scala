package disco

import discord4j.core.`object`.reaction.ReactionEmoji
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono

object Commands {
  type Command = MessageCreateEvent => Mono[Void]

  def ping (event: MessageCreateEvent): Mono[Void] =
    event.getMessage.getChannel
      .flatMap(_.createMessage("Pong!"))
      .`then`()

  def pong (event: MessageCreateEvent): Mono[Void] =
    event.getMessage
      .addReaction(ReactionEmoji.unicode(Emoji.EYES))
      .`then`()

  val commands = Map[String, Command](
    "ping" -> ping,
    "pong" -> pong,
  )
}
