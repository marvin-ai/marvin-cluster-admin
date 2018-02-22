/*
 * Copyright [2017] [B2W Digital]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.marvin.cluster.api

import akka.actor.{ActorRef, ActorSystem, Props, Terminated}
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import org.marvin.cluster.api.AdminHttpAPI.DefaultHttpResponse
import org.marvin.cluster.exception.EngineExceptionAndRejectionHandler._
import org.marvin.cluster.manager.executor.ExecutorManagerClient
import org.marvin.cluster.manager.executor.ExecutorManagerClient.GetMetadata
import spray.json._

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.{Failure, Success}

trait AdminHttpAPIFunctions {
  def engines(): Future[String]
  def setupSystem(engineFilePath:String, paramsFilePath:String, modelProtocol:String): ActorSystem
  def startServer(ipAddress: String, port: Int, system: ActorSystem): Unit
  def terminate(): Future[Terminated]
  def routes: Route
}

object AdminHttpAPI {
  case class DefaultHttpResponse(result: String)
}

class AdminHttpAPI() extends HttpMarvinApp with SprayJsonSupport with DefaultJsonProtocol {

  var system: ActorSystem = _
  var log: LoggingAdapter = _
  var executorManagerClient: ActorRef = _
  var defaultTimeout:Timeout = _

  implicit val responseFormat = jsonFormat1(DefaultHttpResponse)

  def routes: Route =
    handleRejections(marvinEngineRejectionHandler){
      handleExceptions(marvinEngineExceptionHandler){
        get {
          path("engines") {
            val responseFuture = engines()
            onComplete(responseFuture){maybeResponse =>
              maybeResponse match{
                case Success(response) => complete(DefaultHttpResponse(response))
                case Failure(e) => failWith(e)
              }
            }
          }
        }
      }
    }

  def engines(): Future[String] = {
    log.info(s"Request for list engines received.")
    implicit val ec = system.dispatchers.lookup("marvin-online-dispatcher")
    implicit val futureTimeout = defaultTimeout
    (executorManagerClient ? GetMetadata).mapTo[String]
  }

  def setupSystem(engineFilePath:String, paramsFilePath:String, modelProtocol:String): ActorSystem = {
    val system = ActorSystem(s"MarvinClusterAdminSystem")

    log = Logging.getLogger(system, this)
    defaultTimeout = Timeout(10 seconds)
    executorManagerClient = system.actorOf(Props(new ExecutorManagerClient()), name = "executorMgrClient")

    system
  }

  def startServer(ipAddress: String, port: Int, system: ActorSystem): Unit = {
    scala.sys.addShutdownHook{
      system.terminate()
    }
    startServer(ipAddress, port, system)
  }

  def terminate(): Future[Terminated] = {
    system.terminate()
  }
}