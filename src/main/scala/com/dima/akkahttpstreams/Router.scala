package com.dima.akkahttpstreams

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

object Router {
  val routingTable: Route = {
    routeDefault() ~ routeRecording()
  }

  def routeDefault(): Route = get {
    pathEndOrSingleSlash {
      complete("Welcome to http-streams server")
    }
  }

  def routeRecording(): Route = {
    pathPrefix("api") {
      path(Segment / IntNumber / IntNumber / LongNumber) {
        (version, subscriberId, projectId, sessionId) => {
          get {
            // complete((writer ? writeMessage).mapTo[String])
            complete("done")
          } ~ post {
            entity(as[String]) { data =>
              complete("done")
            }
          }
        }
      }
    }
  }
}

