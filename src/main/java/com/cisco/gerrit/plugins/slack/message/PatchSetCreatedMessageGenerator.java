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

package com.cisco.gerrit.plugins.slack.message;

import static org.apache.commons.lang.StringUtils.substringBefore;

import com.cisco.gerrit.plugins.slack.config.ProjectConfig;
import com.google.gerrit.server.events.PatchSetCreatedEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A specific MessageGenerator implementation that can generate a message for a patchset created
 * event.
 *
 * @author Matthew Montgomery
 */
public class PatchSetCreatedMessageGenerator implements MessageGenerator {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(PatchSetCreatedMessageGenerator.class);

  private PatchSetCreatedEvent event;
  private ProjectConfig config;

  /**
   * Creates a new PatchSetCreatedMessageGenerator instance using the provided PatchSetCreatedEvent
   * instance.
   *
   * @param event The PatchSetCreatedEvent instance to generate a message for.
   */
  PatchSetCreatedMessageGenerator(PatchSetCreatedEvent event, ProjectConfig config) {
    if (event == null) {
      throw new NullPointerException("event cannot be null");
    }

    this.event = event;
    this.config = config;
  }

  @Override
  public boolean shouldPublish() {
    if (!config.isEnabled() || !config.shouldPublishOnPatchSetCreated()) {
      return false;
    }

    boolean result;
    result = true;

    try {
      Pattern pattern;
      pattern = Pattern.compile(config.getIgnore(), Pattern.DOTALL);

      Matcher matcher;
      matcher = pattern.matcher(event.change.get().commitMessage);

      // If the ignore pattern matches, publishing should not happen
      result = !matcher.matches();
    } catch (Exception e) {
      LOGGER.warn("The specified ignore pattern was invalid", e);
    }

    return result;
  }

  @Override
  public String generate() {
    String message;
    message = "";

    try {
      MessageTemplate template;
      template = new MessageTemplate();

      template.setChannel(config.getChannel());
      template.setName(event.uploader.get().name);
      template.setAction("proposed");
      template.setNumber(event.change.get().number);
      template.setProject(event.change.get().project);
      template.setBranch(event.change.get().branch);
      template.setUrl(event.change.get().url);
      template.setTitle(substringBefore(event.change.get().commitMessage, "\n"));

      message = template.render();
    } catch (Exception e) {
      LOGGER.error("Error generating message: " + e.getMessage(), e);
    }

    return message;
  }
}
