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
package org.marvin.executor.api

import akka.actor.{ActorRef, ActorSystem, Props, Terminated}
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import org.marvin.cluster.api.HttpMarvinApp
import org.marvin.cluster.manager.executor.ExecutorManagerClient
import org.marvin.cluster.manager.executor.ExecutorManagerClient.GetMetadata
import org.marvin.cluster.api.exception.EngineExceptionAndRejectionHandler._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class AdminHttpAPIImpl() extends AdminHttpAPI

object AdminHttpAPI extends HttpMarvinApp with SprayJsonSupport with DefaultJsonProtocol {
  var system: ActorSystem = _
  var log: LoggingAdapter = _
  var api: AdminHttpAPI = new AdminHttpAPIImpl()
  var executorManagerClient: ActorRef = _
  var defaultTimeout:Timeout = _

  case class DefaultHttpResponse(result: String)
  implicit val responseFormat = jsonFormat1(DefaultHttpResponse)

  override def routes: Route =
    handleRejections(marvinEngineRejectionHandler){
      handleExceptions(marvinEngineExceptionHandler){
        get {
          path("engines") {
            val responseFuture = api.engines()
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

  def main(args: Array[String]): Unit = {
    api.startServer("localhost", 8010, AdminHttpAPI.system)
  }
}

trait AdminHttpAPI {
  protected def engines(): Future[String] = {
    AdminHttpAPI.log.info(s"Request for list engines received.")
    implicit val ec = AdminHttpAPI.system.dispatchers.lookup("marvin-online-dispatcher")
    implicit val futureTimeout = AdminHttpAPI.defaultTimeout
    (AdminHttpAPI.executorManagerClient ? GetMetadata).mapTo[String]
  }

  protected def setupSystem(engineFilePath:String, paramsFilePath:String, modelProtocol:String): ActorSystem = {
    val system = ActorSystem(s"MarvinClusterAdminSystem")

    AdminHttpAPI.log = Logging.getLogger(system, this)
    AdminHttpAPI.defaultTimeout = Timeout(10 seconds)
    AdminHttpAPI.executorManagerClient = system.actorOf(Props(new ExecutorManagerClient()), name = "executorMgrClient")

    system
  }

  protected def startServer(ipAddress: String, port: Int, system: ActorSystem): Unit = {
    scala.sys.addShutdownHook{
      system.terminate()
    }
    AdminHttpAPI.startServer(ipAddress, port, system)
  }

  protected def terminate(): Future[Terminated] = {
    AdminHttpAPI.system.terminate()
  }

}