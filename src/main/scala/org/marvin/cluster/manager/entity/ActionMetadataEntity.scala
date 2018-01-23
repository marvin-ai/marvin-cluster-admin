package org.marvin.cluster.manager.entity

import javax.persistence._
import scala.beans.BeanProperty


@Entity
@Table(name = "engineActionMetadata")
class ActionMetadataEntity(name: String, aType: String, port: Int, host: String, aToPersist: String, aToLoad: String, x: MetadataEntity) {

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
  @JoinColumn(name="metadataID")
  var metadataID: MetadataEntity = x
//
//  def this() = this(null, null, 0, null, null, null)
//
//  override def toString = id + " = " + metadataID

}
