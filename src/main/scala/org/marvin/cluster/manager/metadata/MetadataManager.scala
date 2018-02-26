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
package org.marvin.cluster.manager.metadata

import akka.Done
import akka.actor.{Actor, ActorLogging}
import org.marvin.cluster.manager.metadata.MetadataManager.{GetById, Save}
import org.marvin.cluster.manager.metadata.entity.MetadataEntity
import org.marvin.model.EngineMetadata
import org.springframework.context.support.ClassPathXmlApplicationContext

object MetadataManager {
  case class Save(metadata: EngineMetadata)
  case class GetById(id: Int)
}

class MetadataManager() extends Actor with ActorLogging {

  var dao: MetadataDao = _

  override def preStart() = {
    log.info(s"${this.getClass().getCanonicalName} actor initialized...")
    val ctx = new ClassPathXmlApplicationContext("application-context.xml")
    dao = ctx.getBean(classOf[MetadataDao])
  }

  override def receive  = {
    case Save(metadata) =>
      log.info("Save message received...")

      dao.save(MetadataEntity.getInstance(metadata))
      sender ! Done

    case GetById(id) =>
      log.info("GetById message received...")

      dao.getByID(id) match {
        case Some(metadata) =>
          sender ! metadata.getId()

        case None =>
          log.info("No engine found with id: " + id)
          sender ! None
      }
  }
}