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

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.marvin.cluster.api.AdminHttpAPIFunctions
import org.marvin.exception.MarvinEExecutorException
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class ClusterAdminAppTest extends
  TestKit(ActorSystem("ClusterAdminAppTest", ConfigFactory.parseString("""akka.loggers = ["akka.testkit.TestEventListener"]""")))
  with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll with MockFactory {

  "getVMParameters method" should {

    "with valid filePath" in {
      val app = new ClusterAdminApp()

      app.vmParams = Map[String, Any]("paramsFilePath" -> getClass.getResource("/engine_home/engine.params").getPath())

      val vmParams = app.getVMParameters()

      vmParams shouldEqual Map[String, Any](
        "engineHome" -> "a/fake/path",
        "ipAddress" -> "1.1.1.1",
        "port" -> 9999,
        "protocol" -> "",
        "enableAdmin" -> false,
        "adminPort" -> 50100,
        "adminHost" -> "127.0.0.1"
      )
    }
  }

  "readJsonIfFileExists method" should {

    "read file successfully" in {
      val app = new ClusterAdminApp()
      val obj = app.readJsonIfFileExists[Map[String, String]](getClass.getResource("/test.json").getPath())
      obj("test") shouldEqual "ok"
    }

    "throw exception trying to read inexistent file" in {
      val app = new ClusterAdminApp()

      val caught = intercept[MarvinEExecutorException] {
        app.readJsonIfFileExists[Map[String, String]]("invalid_path/test.json")
      }

      caught.getMessage() shouldEqual "The file [invalid_path/test.json] does not exists. Check your engine configuration."
    }

    "throw exception trying to read invalid file" in {
      val app = new ClusterAdminApp()
      val filePath = getClass.getResource("/test-invalid.json").getPath()

      val caught = intercept[MarvinEExecutorException] {
        app.readJsonIfFileExists[Map[String, String]](filePath)
      }

      caught.getMessage() shouldEqual s"The file [$filePath] is an invalid json file. Check your file syntax!"
    }
  }

  "getEngineMetadata method" should {

    "with valid filePath" in {
      val app = new ClusterAdminApp()

      app.vmParams = Map[String, Any]("engineHome" -> getClass.getResource("/engine_home").getPath())

      val metadata = app.getEngineMetadata()

      metadata.name shouldEqual "teste_engine"
    }

    "with invalid filePath" in {
      val app = new ClusterAdminApp()

      app.vmParams = Map[String, Any]("engineHome" -> "invalid_path")

      val caught = intercept[MarvinEExecutorException] {
        app.getEngineMetadata()
      }

      caught.getMessage() shouldEqual "The file [invalid_path/engine.metadata] does not exists. Check your engine configuration."
    }

    "without a correct vmParameter key" in {
      val app = new ClusterAdminApp()

      app.vmParams = Map[String, Any]()

      val caught = intercept[NoSuchElementException] {
        app.getEngineMetadata()
      }

      caught.getMessage() shouldEqual "key not found: engineHome"
    }
  }

  "start method" should {

    "works successfully" in {
      val app = new ClusterAdminApp()
      val mockApi = mock[AdminHttpAPIFunctions]
      val host = "127.0.0.99"
      val port = 9991

      app.vmParams = Map[String, Any]("port" -> port, "ipAddress" -> host)
      app.api = mockApi

      (mockApi.startServer(_: String, _: Int)).expects(host, port).once()

      app.start(app.api)
    }
  }

}
