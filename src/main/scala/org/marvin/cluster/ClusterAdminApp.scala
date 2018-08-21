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
package org.marvin.cluster

import java.io.FileNotFoundException
import akka.actor.{ActorSystem, Props}
import com.fasterxml.jackson.core.JsonParseException
import com.github.fge.jsonschema.core.exceptions.ProcessingException
import grizzled.slf4j.Logger
import org.marvin.cluster.api.{AdminHttpAPI, AdminHttpAPIFunctions}
import org.marvin.cluster.manager.executor.ExecutorManagerClient
import org.marvin.exception.MarvinEExecutorException
import org.marvin.model.EngineMetadata
import org.marvin.util.{ConfigurationContext, JsonUtil}
import scala.io.Source
import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

object ClusterAdminApp {
  def main(args: Array[String]): Unit = {

    val app = new ClusterAdminApp()
    val api = app.setupAdminHttpAPI()

    app.start(api)

  }
}

class ClusterAdminApp {

  var vmParams: Map[String, Any] = getVMParameters
  var api: AdminHttpAPIFunctions = _
  var system: ActorSystem = _
  lazy val log = Logger[this.type]

  def start(api: AdminHttpAPIFunctions) = {
    log.info("Starting Admin Http API ...")
    api.startServer(vmParams("ipAddress").asInstanceOf[String], vmParams("port").asInstanceOf[Int])
  }

  def setupAdminHttpAPI(): AdminHttpAPIFunctions = {

    val metadata = getEngineMetadata()
    val system = ActorSystem(metadata.name)
    val actor = system.actorOf(Props(new ExecutorManagerClient), name = "executorManagerClient")

    api = new AdminHttpAPI(system, metadata, actor)

    api
  }

  def getEngineMetadata(): EngineMetadata = {
    log.info("Getting metadata file from engine...")

    val filePath = s"${vmParams("engineHome").asInstanceOf[String]}/engine.metadata"
    readJsonIfFileExists[EngineMetadata](filePath, true)
  }

  def getVMParameters(): Map[String, Any] = {

    log.info("Getting vm parameters...")

    //Get all VM options
    val parameters = Map[String, Any](
      "engineHome" -> s"${ConfigurationContext.getStringConfigOrDefault("engineHome", "")}",
      "ipAddress" -> ConfigurationContext.getStringConfigOrDefault("ipAddress", "localhost"),
      "port" -> ConfigurationContext.getIntConfigOrDefault("port", 8000),
      "protocol" -> ConfigurationContext.getStringConfigOrDefault("protocol", ""),
      "enableAdmin" -> ConfigurationContext.getBooleanConfigOrDefault("enableAdmin", false),
      "adminPort" -> ConfigurationContext.getIntConfigOrDefault("adminPort", 50100),
      "adminHost" -> ConfigurationContext.getStringConfigOrDefault("adminHost", "127.0.0.1")
    )

    parameters
  }

  def readJsonIfFileExists[T: ClassTag](filePath: String, validate: Boolean = false): T = {

    log.info(s"Reading json file from [$filePath]...")

    Try(JsonUtil.fromJson[T](Source.fromFile(filePath).mkString, validate)) match {
      case Success(json) => json
      case Failure(ex) => {
        ex match {
          case ex: FileNotFoundException => throw new MarvinEExecutorException(s"The file [$filePath] does not exists." +
            s" Check your engine configuration.", ex)
          case ex: ProcessingException => throw new MarvinEExecutorException(s"The file [$filePath] is invalid."  +
            s" Check your file!", ex)
          case ex: JsonParseException => throw new MarvinEExecutorException(s"The file [$filePath] is an invalid json file."  +
            s" Check your file syntax!", ex)
          case _ => throw ex
        }
      }
    }
  }
}

