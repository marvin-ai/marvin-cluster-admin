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

import javax.persistence.EntityManager

import org.marvin.cluster.manager.entity.MetadataEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.{Propagation, Transactional}


@Repository("MetadataDao")
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
class MetadataDao {

  @Autowired
  var entityManager: EntityManager = _

  @Autowired
  var customDataSource: DataSource = _

  def save(entity: MetadataEntity): Unit = entity.id match {
    case 0 => entityManager.persist(entity)
    case _ => entityManager.merge(entity)
  }

}
