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
package org.marvin.cluster.manager.datasource

import java.io.FileInputStream
import java.util.Properties

import org.marvin.util.ConfigurationContext

class DefaultDataSource() extends org.springframework.jdbc.datasource.DriverManagerDataSource {

  val filePath = s"${ConfigurationContext.getStringConfigOrDefault("confFile", getClass.getResource("/marvin.ini").getPath)}"
  val prop = new Properties()

  prop.load(new FileInputStream(filePath))

  setUrl(prop.get("jdbc.url").toString)
  setUsername(prop.get("jdbc.username").toString)
  setPassword(prop.get("jdbc.password").toString)
  setDriverClassName(prop.get("jdbc.driverClassName").toString)
}
