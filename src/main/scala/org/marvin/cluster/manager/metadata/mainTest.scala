package org.marvin.cluster.manager.metadata

import akka.actor.{ActorSystem, Props}
import org.marvin.cluster.manager.metadata.MetadataManager.Save
import org.marvin.model.{EngineActionMetadata, EngineMetadata}


object mainTest {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem(s"MarvinClusterAdminSystem")
    val testActor = system.actorOf(Props(new MetadataManager()), name = "metadataManager")

    val metadata = EngineMetadata(
      name = "test",
      actions = List[EngineActionMetadata](
        new EngineActionMetadata(name="predictor", actionType="online", port=777, host="localhost", artifactsToPersist=List(), artifactsToLoad=List("model")),
        new EngineActionMetadata(name="acquisitor", actionType="batch", port=778, host="localhost", artifactsToPersist=List("initial_dataset"), artifactsToLoad=List()),
        new EngineActionMetadata(name="tpreparator", actionType="batch", port=779, host="localhost", artifactsToPersist=List("dataset"), artifactsToLoad=List("initial_dataset")),
        new EngineActionMetadata(name="trainer", actionType="batch", port=780, host="localhost", artifactsToPersist=List("model"), artifactsToLoad=List("dataset")),
        new EngineActionMetadata(name="evaluator", actionType="batch", port=781, host="localhost", artifactsToPersist=List("metrics"), artifactsToLoad=List("dataset", "model"))
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

    testActor ! Save(metadata)

  }

}
