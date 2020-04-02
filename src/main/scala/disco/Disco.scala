package disco

import disco.Commands.commands
import discord4j.core.`object`.util.Snowflake
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.{DiscordClient, DiscordClientBuilder}
import org.slf4j.LoggerFactory
import reactor.core.publisher.{Flux, Mono}

object Disco extends App {
  val client = new DiscordClientBuilder(sys.env("BOT_TOKEN")).build
  val logger = LoggerFactory.getLogger("disco.Disco")

  client.getEventDispatcher.on(classOf[ReadyEvent])
    .subscribe((ready: ReadyEvent) => {
      val botId = ready.getSelf.getId
      setupHandlers(botId, client)
      logger.info(s"Logged in as ${ready.getSelf.getUsername}")
    })

  def setupHandlers (botId: Snowflake, client: DiscordClient) = {
    def parseContent (text: String): String = {
      println(text)
      text.replaceAll(s"<@!${botId.asString}>", "")
          .replaceAll(s"<@${botId.asString}>", "")
          .trim
    }

    def handleCommand (event: MessageCreateEvent)(cmd: String): Mono[Void] = {
      logger.debug(s"Command: '$cmd'")
      commands get cmd match {
        case None     => Flux.empty().next()
        case Some(fn) => fn(event)
      }
    }

    client.getEventDispatcher.on(classOf[MessageCreateEvent])
      .filter(_.getMessage.getUserMentionIds contains botId)
      .flatMap((e: MessageCreateEvent) =>
        Mono.justOrEmpty(e.getMessage.getContent map (parseContent(_)))
            .flatMap(handleCommand(e))
      )
      .subscribe()
  }

  client.login().block()
}
