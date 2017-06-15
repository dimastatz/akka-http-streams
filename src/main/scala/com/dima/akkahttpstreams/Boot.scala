package com.dima.akkahttpstreams

import akka.stream._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.LazyLogging

object Boot extends LazyLogging{
  def main(args: Array[String]): Unit = {
    import system.dispatcher
    implicit val system = ActorSystem("akka-http-streams")
    implicit val materializer = initMaterializer()

    logger.debug("starting app")
    val binding = Http().bindAndHandle(Router.routingTable, "0.0.0.0", 8080)

    sys addShutdownHook {
      logger.debug(s"Cage service is terminating.")
      binding.flatMap(_.unbind()).onComplete(_ => system.terminate())
    }
  }

  private def initMaterializer()(implicit system: ActorSystem) = {
    val decider: Supervision.Decider = {
      case _: Exception => Supervision.Resume
      case _ => Supervision.Stop
    }
    val settings = ActorMaterializerSettings(system)
    ActorMaterializer(settings.withSupervisionStrategy(decider))
  }
}
