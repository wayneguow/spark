/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.execution.command.v1

import org.apache.spark.sql.AnalysisException
import org.apache.spark.sql.execution.command

/**
 * This base suite contains unified tests for the `SHOW COLUMNS ...` command that check V1 table
 * catalogs. The tests that cannot run for all V1 catalogs are located in more specific test suites:
 *
 *   - V1 In-Memory catalog:
 *      `org.apache.spark.sql.execution.command.v1.ShowColumnsSuite`
 *   - V1 Hive External catalog:
 *     `org.apache.spark.sql.hive.execution.command.ShowColumnsSuite`
 */
trait ShowColumnsSuiteBase extends command.ShowColumnsSuiteBase {

  test("invalid db name") {
    withNamespaceAndTable("ns", "tbl") { t =>
      sql(s"CREATE TABLE $t(col1 int, col2 string) $defaultUsing")
      checkError(
        exception = intercept[AnalysisException] {
          sql("SHOW COLUMNS IN tbl FROM a.b.c")
        },
        condition = "REQUIRES_SINGLE_PART_NAMESPACE",
        parameters = Map(
          "sessionCatalog" -> catalog,
          "namespace" -> "`a`.`b`.`c`"
        )
      )
    }
  }
}

/**
 * The class contains tests for the `SHOW COLUMNS ...` command to check V1 In-Memory
 * table catalog.
 */
class ShowColumnsSuite extends ShowColumnsSuiteBase with CommandSuiteBase
