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

import akka.actor.{ActorRef, ActorSystem, Props, Terminated}
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import org.marvin.cluster.api.HttpMarvinApp
import org.marvin.cluster.manager.executor.ExecutorManagerClient
import org.marvin.cluster.manager.executor.ExecutorManagerClient.GetMetadata
import org.marvin.cluster.exception.EngineExceptionAndRejectionHandler._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object ClusterAdminApp {

}

