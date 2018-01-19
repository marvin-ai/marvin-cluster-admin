package org.marvin.cluster.cli

import akka.Done
import akka.actor.{ActorRef, ActorSystem}
import com.typesafe.config.ConfigFactory
import org.marvin.executor.manager.ExecutorManager
import org.marvin.model.EngineMetadata
import org.marvin.util.JsonUtil
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * Created by taka on 16/01/18.
  */
object TestCluster {
  def main(args: Array[String]): Unit = {

    val customConf = ConfigFactory.parseString(
      """
        akka{
          actor {
            warn-about-java-serializer-usage = off
            provider = remote
          }

          remote.artery {
            enabled = on
            canonical.hostname = "127.0.0.1"
 |          canonical.port = 50200
          }
        }
      """
    ).withFallback(ConfigFactory.load())

    // ConfigFactory.load sandwiches customConfig between default reference
    // config and default overrides, and then resolves it.
    val system = ActorSystem("MySystem", customConf)
    implicit val ec = system.dispatcher
    implicit val futureTimeout = Timeout(30 seconds)

    val remoteSelection = system.actorSelection("akka://iris_species@127.0.0.1:50100/user/executorManager")

    remoteSelection.resolveOne(futureTimeout.duration).onComplete {
      case Success(remoteActorRef) =>
        //getMetadata(system, remoteActorRef)
        stopActor(system, remoteActorRef, "predictor")
        stopActor(system, remoteActorRef, "pipeline")

      case Failure(ex) =>
        ex.printStackTrace()
    }


  }

  private def getMetadata(system: ActorSystem, remoteActorRef: ActorRef) = {
    implicit val ec = system.dispatcher
    implicit val futureTimeout = Timeout(30 seconds)

    system.log.info(s"Actor ${remoteActorRef.path} found. Trying to send message to get metadata..")
    val metadata = (remoteActorRef ? ExecutorManager.GetMetadata).mapTo[EngineMetadata]
    print(JsonUtil.toJson(metadata))
  }

  private def stopActor(system: ActorSystem, remoteActorRef: ActorRef, actionName: String) = {
    implicit val ec = system.dispatcher
    implicit val futureTimeout = Timeout(30 seconds)

    system.log.info(s"Actor ${remoteActorRef.path} found. Trying to send message to stop actor..")
    val status = (remoteActorRef ? ExecutorManager.StopActor(actionName)).mapTo[String]
    system.log.info(s"Actor stoped... Status: ${status}")
  }
}
