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
package org.marvin.cluster.metadata

import akka.Done
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.marvin.cluster.manager.metadata.MetadataManager
import org.marvin.cluster.manager.metadata.MetadataManager.{GetById, Save}
import org.marvin.fixtures.MetadataMock
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

class MetadataManagerTest extends TestKit(
  ActorSystem("MetadataManagerTest", ConfigFactory.parseString("""akka.loggers = ["akka.testkit.TestEventListener"]""")))
  with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "metadata manager actor" must {

    "receive Save message" in {

      val actor = system.actorOf(Props(new MetadataManager()))
      val metadata = MetadataMock.simpleMockedMetadata()

      actor ! Save(metadata)

      within(30000 millis) {
        expectMsg(Done)
      }
    }

    "receive GetById message with a valid id" in {

      val actor = system.actorOf(Props(new MetadataManager()))
      val metadata = MetadataMock.simpleMockedMetadata()

      actor ! Save(metadata)
      within(30000 millis) {
        expectMsg(Done)
      }

      actor ! GetById(1)
      within(30000 millis) {
        expectMsg(1)
      }
    }

    "receive GetById message with a invalid id" in {

      val actor = system.actorOf(Props(new MetadataManager()))

      actor ! GetById(3)
      within(30000 millis) {
        expectMsg(None)
      }
    }
  }
}
