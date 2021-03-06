/*
 * Copyright (c) 2016 Uber Technologies, Inc. (hoodie-dev-group@uber.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uber.hoodie.config;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Maps;
import com.uber.hoodie.config.HoodieWriteConfig.Builder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import org.junit.Test;

public class HoodieWriteConfigTest {

  @Test
  public void testPropertyLoading() throws IOException {
    Builder builder = HoodieWriteConfig.newBuilder().withPath("/tmp");
    Map<String, String> params = Maps.newHashMap();
    params.put(HoodieCompactionConfig.MAX_COMMITS_TO_KEEP, "5");
    params.put(HoodieCompactionConfig.MIN_COMMITS_TO_KEEP, "2");
    ByteArrayOutputStream outStream = saveParamsIntoOutputStream(params);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(outStream.toByteArray());
    try {
      builder = builder.fromInputStream(inputStream);
    } finally {
      outStream.close();
      inputStream.close();
    }
    HoodieWriteConfig config = builder.build();
    assertEquals(config.getMaxCommitsToKeep(), 5);
    assertEquals(config.getMinCommitsToKeep(), 2);
  }

  private ByteArrayOutputStream saveParamsIntoOutputStream(Map<String, String> params) throws IOException {
    Properties properties = new Properties();
    properties.putAll(params);
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    properties.store(outStream, "Saved on " + new Date(System.currentTimeMillis()));
    return outStream;
  }
}
