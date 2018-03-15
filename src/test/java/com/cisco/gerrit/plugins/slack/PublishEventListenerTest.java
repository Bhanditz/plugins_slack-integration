/*
 * Copyright 2017 Cisco Systems, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */

package com.cisco.gerrit.plugins.slack;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.google.gerrit.server.events.ChangeMergedEvent;
import com.google.gerrit.server.events.PatchSetCreatedEvent;
import org.junit.Before;
import org.junit.Test;

public class PublishEventListenerTest {
  private PatchSetCreatedEvent mockPatchSetCreatedEvent = mock(PatchSetCreatedEvent.class);
  private ChangeMergedEvent mockChangeMergedEvent = mock(ChangeMergedEvent.class);

  private PublishEventListener publishEventListener;

  @Before
  public void setup() throws Exception {
    publishEventListener = new PublishEventListener();
  }

  @Test
  public void handlesPatchSetCreatedEvents() throws Exception {
    // TODO: Add actual tests here
    assertTrue(true);
  }
}
