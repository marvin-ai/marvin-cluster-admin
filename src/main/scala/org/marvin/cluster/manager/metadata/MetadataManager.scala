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
import org.marvin.cluster.manager.entity.MetadataEntity
import org.marvin.cluster.manager.metadata.MetadataManager.Save
import org.marvin.model.EngineMetadata
import org.springframework.context.support.ClassPathXmlApplicationContext

object MetadataManager {
  case class Save(metadata: EngineMetadata)
}

class MetadataManager() extends Actor with ActorLogging {

  val ctx = new ClassPathXmlApplicationContext("application-context.xml")

  override def receive  = {
    case Save(metadata) =>
      log.info("Message Received!")
      val entity = MetadataEntity.getInstance(metadata)

      val metaDataSaver: MetadataDao = ctx.getBean(classOf[MetadataDao])

      metaDataSaver.save(entity)

      sender ! Done

  }
}

