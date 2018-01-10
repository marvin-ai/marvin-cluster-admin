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
package org.marvin.cluster.manager.executor

import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout
import org.marvin.cluster.manager.executor.ExecutorManagerClient.GetMetadata

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object ExecutorManagerClient {
  case class GetMetadata()
}

class ExecutorManagerClient() extends Actor with ActorLogging {
  implicit val ec = ExecutionContext.global
  implicit val futureTimeout = Timeout(30 seconds)

  override def receive  = {
    case GetMetadata =>
      log.info("Message Received!")
      sender ! "Done"

  }
}