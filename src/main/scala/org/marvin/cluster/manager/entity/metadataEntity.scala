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

import java.util
import javax.persistence._

import org.marvin.model.EngineMetadata

import scala.beans.BeanProperty

object MetadataEntity {
  def getInstance(metadata: EngineMetadata): MetadataEntity = {

    val metadataEntity = new MetadataEntity(name = metadata.name, version = metadata.version, enType = metadata.engineType, artifactsRPath = metadata.artifactsRemotePath,
      artifactMType = metadata.artifactManagerType, bucketName = metadata.s3BucketName, pipeActions = metadata.pipelineActions.mkString(","),
      onlineTimeout = metadata.onlineActionTimeout, healthTimeout = metadata.healthCheckTimeout, rlTimeout = metadata.reloadTimeout,
      rlStateTimeout = metadata.reloadStateTimeout.get, btActionTimeout = metadata.batchActionTimeout, hfHost = metadata.hdfsHost,
      acts = new util.ArrayList[ActionMetadataEntity])

    val actionEntity: java.util.List[ActionMetadataEntity] = new util.ArrayList[ActionMetadataEntity]
    for (action <- metadata.actions) {actionEntity.add(ActionMetadataEntity.getInstance(action, metadataEntity))}

    metadataEntity.actions = actionEntity
    metadataEntity

  }
}

@Entity
@Table(name = "engineMetadata")
class MetadataEntity(name: String, version: String, enType: String, artifactsRPath: String, artifactMType: String, bucketName: String,
                     pipeActions: String, onlineTimeout: Int, healthTimeout: Int, rlTimeout: Int, rlStateTimeout: Int,
                     btActionTimeout: Int, hfHost: String, acts: java.util.List[ActionMetadataEntity]) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @BeanProperty
  var id: Int = _

  @BeanProperty
  var engineName: String = name

  @BeanProperty
  var engineVersion: String = version

  @BeanProperty
  var engineType: String = enType

  @BeanProperty
  var artifactsRemotePath: String = artifactsRPath

  @BeanProperty
  var artifactManagerType: String = artifactMType

  @BeanProperty
  var s3BucketName: String = bucketName

  @BeanProperty
  var pipelineActions: String = pipeActions

  @BeanProperty
  var onlineActionTimeout: Int = onlineTimeout

  @BeanProperty
  var healthCheckTimeout: Int = healthTimeout

  @BeanProperty
  var reloadTimeout: Int = rlTimeout

  @BeanProperty
  var reloadStateTimeout: Int = rlStateTimeout

  @BeanProperty
  var batchActionTimeout: Int = btActionTimeout

  @BeanProperty
  var hdfsHost: String = hfHost

  @OneToMany(mappedBy = "metadataID", targetEntity = classOf[ActionMetadataEntity], fetch = FetchType.LAZY, cascade = Array(CascadeType.ALL))
  @BeanProperty
  var actions: java.util.List[ActionMetadataEntity] = acts

  def this() = this(null, null, null, null, null, null, null, 0, 0, 0, 0, 0, null, null)

  override def toString = "id: " + id + " = " + engineName + " " + engineVersion + " " + engineType + " " +
    artifactsRemotePath + " " + artifactManagerType + " " + s3BucketName + " " + pipelineActions + " " +
    onlineActionTimeout + " " + healthCheckTimeout + reloadTimeout + " " + reloadStateTimeout + " " +
    batchActionTimeout + " " + hdfsHost + " "

}
