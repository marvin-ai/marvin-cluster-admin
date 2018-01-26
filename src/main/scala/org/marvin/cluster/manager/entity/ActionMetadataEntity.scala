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
package org.marvin.cluster.manager.entity

import javax.persistence._

import org.marvin.model.EngineActionMetadata

import scala.beans.BeanProperty

object ActionMetadataEntity {
  def getInstance(actionMetadata: EngineActionMetadata, mtdataEntity: MetadataEntity): ActionMetadataEntity = {
    new ActionMetadataEntity(name = actionMetadata.name, aType = actionMetadata.actionType, port = actionMetadata.port, host = actionMetadata.host,
      aToPersist = actionMetadata.artifactsToPersist.mkString(","), aToLoad = actionMetadata.artifactsToLoad.mkString(","), metadataEntity = mtdataEntity)
  }
}

@Entity
@Table(name = "engineActionMetadata")
class ActionMetadataEntity(name: String, aType: String, port: Int, host: String, aToPersist: String, aToLoad: String, metadataEntity: MetadataEntity) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @BeanProperty
  var id: Int = _

  @BeanProperty
  var actionName: String = name

  @BeanProperty
  var actionType: String = aType

  @BeanProperty
  var actionPort: Int = port

  @BeanProperty
  var actionHost: String = host

  @BeanProperty
  var artifactsToPersist: String = aToPersist

  @BeanProperty
  var artifactsToLoad: String = aToLoad

  @BeanProperty
  @ManyToOne
  @JoinColumn(name="metadata_id")
  var metadataID: MetadataEntity = metadataEntity

}
