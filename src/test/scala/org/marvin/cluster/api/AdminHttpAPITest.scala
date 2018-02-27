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

import akka.http.scaladsl.model.{ContentTypes, StatusCode}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.TestProbe
import org.marvin.cluster.api.AdminHttpAPI
import org.marvin.cluster.manager.executor.ExecutorManagerClient.GetMetadata
import org.marvin.fixtures.MetadataMock
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Inside, Matchers, WordSpec}

class AdminHttpAPITest extends WordSpec with ScalatestRouteTest with Matchers with Inside with MockFactory {

  val metadata = MetadataMock.simpleMockedMetadata()
  val testActors = TestProbe()

  var api: AdminHttpAPI = new AdminHttpAPI(system, metadata, testActors.ref)
  val route = api.routes

  "/engines endpoint" should {

    "interpret the input message and respond with media type json" in {

      val response = "Done"

      val result = Get("/engines") ~> route ~> runRoute

      testActors.expectMsg(GetMetadata)
      testActors.reply(response)

      check {
        status shouldEqual StatusCode.int2StatusCode(200)
        contentType shouldEqual ContentTypes.`application/json`
        responseAs[String] shouldEqual s"""{"result":"$response"}"""
      }(result)
    }
  }
}
