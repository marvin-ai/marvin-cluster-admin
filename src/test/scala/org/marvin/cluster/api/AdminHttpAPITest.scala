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

import java.text.BreakIterator

import akka.Done
import akka.event.Logging
import akka.http.scaladsl.model.{ContentTypes, StatusCode}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.TestProbe
import akka.util.Timeout
import org.marvin.cluster.manager.executor.ExecutorManagerClient.GetMetadata

import scala.concurrent.duration._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Inside, Matchers, WordSpec}

class AdminHttpAPITest extends WordSpec with ScalatestRouteTest with Matchers with Inside with MockFactory {

  val route = AdminHttpAPI.routes
  val testActors = setupAdminHttpAPIActors()

  "/engines endpoint" should {

    "interpret the input message and respond with media type json" in {

      val response = "Done"

      val result = Get("/engines") ~> route ~> runRoute

      testActors("executorManagerClient").expectMsg(GetMetadata)
      testActors("executorManagerClient").reply(response)

      check {
        status shouldEqual StatusCode.int2StatusCode(200)
        contentType shouldEqual ContentTypes.`text/plain(UTF-8)`
        responseAs[String] shouldEqual "Done"
      }(result)
    }
  }

  def setupAdminHttpAPIActors(): Map[String, TestProbe] = {
    val timeout = Timeout(3 seconds)
    AdminHttpAPI.system = system
    AdminHttpAPI.defaultTimeout = timeout
    AdminHttpAPI.log = Logging.getLogger(system, this)

    val testActors = Map[String, TestProbe](
      "executorManagerClient" -> TestProbe()
    )

    AdminHttpAPI.executorManagerClient = testActors("executorManagerClient").ref

    testActors
  }
}
