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
package org.marvin.cluster.manager.metadata.entity

import javax.persistence._

import org.marvin.model.EngineActionMetadata

import scala.beans.BeanProperty

object ActionMetadataEntity {
  def getInstance(actionMetadata: EngineActionMetadata): ActionMetadataEntity = {
    new ActionMetadataEntity(
      name = actionMetadata.name,
      actionType = actionMetadata.actionType,
      port = actionMetadata.port,
      host = actionMetadata.host,
      artifactsToPersist = actionMetadata.artifactsToPersist.mkString(","),
      artifactsToLoad = actionMetadata.artifactsToLoad.mkString(","))
  }
}

@Entity
@Table(name = "engine_action_metadata")
class ActionMetadataEntity(@BeanProperty name: String,
                           @BeanProperty actionType: String,
                           @BeanProperty port: Int,
                           @BeanProperty host: String,
                           @BeanProperty artifactsToPersist: String,
                           @BeanProperty artifactsToLoad: String) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @BeanProperty
  var id: Int = _
}