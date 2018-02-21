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
package org.marvin.cluster.manager

import akka.Done
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.marvin.cluster.manager.metadata.MetadataManager
import org.marvin.cluster.manager.metadata.MetadataManager.{GetById, Save}
import org.marvin.model.{EngineActionMetadata, EngineMetadata}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

class MetadataSaverTest extends TestKit(
  ActorSystem("MetadataSaverTest", ConfigFactory.parseString("""akka.loggers = ["akka.testkit.TestEventListener"]""")))
  with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "metadata manager actor" must {

    "send Done message" in {

      val metadataSaver = system.actorOf(Props(new MetadataManager()))

      val metadata = EngineMetadata(
        name = "test",
        actions = List[EngineActionMetadata](
          new EngineActionMetadata(name = "predictor", actionType = "online", port = 777, host = "localhost", artifactsToPersist = List(), artifactsToLoad = List("model")),
          new EngineActionMetadata(name = "acquisitor", actionType = "batch", port = 778, host = "localhost", artifactsToPersist = List("initial_dataset"), artifactsToLoad = List()),
          new EngineActionMetadata(name = "tpreparator", actionType = "batch", port = 779, host = "localhost", artifactsToPersist = List("dataset"), artifactsToLoad = List("initial_dataset")),
          new EngineActionMetadata(name = "trainer", actionType = "batch", port = 780, host = "localhost", artifactsToPersist = List("model"), artifactsToLoad = List("dataset")),
          new EngineActionMetadata(name = "evaluator", actionType = "batch", port = 781, host = "localhost", artifactsToPersist = List("metrics"), artifactsToLoad = List("dataset", "model"))
        ),
        artifactsRemotePath = "",
        artifactManagerType = "HDFS",
        s3BucketName = "marvin-artifact-bucket",
        batchActionTimeout = 100,
        engineType = "python",
        hdfsHost = "",
        healthCheckTimeout = 100,
        onlineActionTimeout = 100,
        pipelineActions = List("acquisitor", "tpreparator"),
        reloadStateTimeout = Some(500),
        reloadTimeout = 100,
        version = "1"
      )

      metadataSaver ! Save(metadata)
      within(8000 millis) {
        expectMsg(Done)
      }
    }

    "print result metadata information and send Done message" in {

      val metadataSaver = system.actorOf(Props(new MetadataManager()))

      val metadata = EngineMetadata(
        name = "test",
        actions = List[EngineActionMetadata](
          new EngineActionMetadata(name = "predictor", actionType = "online", port = 777, host = "localhost", artifactsToPersist = List(), artifactsToLoad = List("model")),
          new EngineActionMetadata(name = "acquisitor", actionType = "batch", port = 778, host = "localhost", artifactsToPersist = List("initial_dataset"), artifactsToLoad = List()),
          new EngineActionMetadata(name = "tpreparator", actionType = "batch", port = 779, host = "localhost", artifactsToPersist = List("dataset"), artifactsToLoad = List("initial_dataset")),
          new EngineActionMetadata(name = "trainer", actionType = "batch", port = 780, host = "localhost", artifactsToPersist = List("model"), artifactsToLoad = List("dataset")),
          new EngineActionMetadata(name = "evaluator", actionType = "batch", port = 781, host = "localhost", artifactsToPersist = List("metrics"), artifactsToLoad = List("dataset", "model"))
        ),
        artifactsRemotePath = "",
        artifactManagerType = "HDFS",
        s3BucketName = "marvin-artifact-bucket",
        batchActionTimeout = 100,
        engineType = "python",
        hdfsHost = "",
        healthCheckTimeout = 100,
        onlineActionTimeout = 100,
        pipelineActions = List("acquisitor", "tpreparator"),
        reloadStateTimeout = Some(500),
        reloadTimeout = 100,
        version = "1"
      )

      metadataSaver ! Save(metadata)
      metadataSaver ! GetById(1)
      within(8000 millis) {
        expectMsg(Done)
      }
    }
  }
}
