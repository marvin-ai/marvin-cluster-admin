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

import java.util
import javax.persistence._

import org.marvin.model.EngineMetadata

import scala.beans.BeanProperty

object MetadataEntity {
  def getInstance(metadata: EngineMetadata): MetadataEntity = {

    val actions: java.util.List[ActionMetadataEntity] = new util.ArrayList[ActionMetadataEntity]
    for (action <- metadata.actions) {actions.add(ActionMetadataEntity.getInstance(action))}

    val metadataEntity = new MetadataEntity(
      name = metadata.name,
      version = metadata.version,
      engineType = metadata.engineType,
      artifactsRemotePath = metadata.artifactsRemotePath,
      artifactManagerType = metadata.artifactManagerType,
      s3BucketName = metadata.s3BucketName,
      pipelineActions = metadata.pipelineActions.mkString(","),
      onlineActionTimeout = metadata.onlineActionTimeout,
      healthCheckTimeout = metadata.healthCheckTimeout,
      reloadTimeout = metadata.reloadTimeout,
      reloadStateTimeout = metadata.reloadStateTimeout.get,
      batchActionTimeout = metadata.batchActionTimeout,
      hdfsHost = metadata.hdfsHost,
      actions = actions
    )
    metadataEntity
  }
}

@Entity
@Table(name = "engine_metadata")
class MetadataEntity(@BeanProperty name: String,
                     @BeanProperty version: String,
                     @BeanProperty engineType: String,
                     @BeanProperty artifactsRemotePath: String,
                     @BeanProperty artifactManagerType: String,
                     @BeanProperty s3BucketName: String,
                     @BeanProperty pipelineActions: String,
                     @BeanProperty onlineActionTimeout: Int,
                     @BeanProperty healthCheckTimeout: Int,
                     @BeanProperty reloadTimeout: Int,
                     @BeanProperty reloadStateTimeout: Int,
                     @BeanProperty batchActionTimeout: Int,
                     @BeanProperty hdfsHost: String,
                     @OneToMany(mappedBy = "metadataID", targetEntity = classOf[ActionMetadataEntity], fetch = FetchType.LAZY, cascade = Array(CascadeType.ALL))
                     @BeanProperty actions: java.util.List[ActionMetadataEntity]) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @BeanProperty
  var id: Int = _

  def this() = this(null, null, null, null, null, null, null, 0, 0, 0, 0, 0, null, null)
}
