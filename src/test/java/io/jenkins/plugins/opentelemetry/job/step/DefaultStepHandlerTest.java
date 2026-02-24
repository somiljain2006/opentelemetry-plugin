/*
 * Copyright The Original Author or Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.jenkins.plugins.opentelemetry.job.step;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import io.jenkins.plugins.opentelemetry.JenkinsOpenTelemetryPluginConfiguration;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class DefaultStepHandlerTest {

    @AfterEach
    void resetConfig() {
        // clean up after each test
        JenkinsOpenTelemetryPluginConfiguration.get().setOmitPipelineStepSpans(false);
    }

    @Test
    void omitPipelineStepSpans_disablesStepSpanCreation(JenkinsRule jenkins) {
        assertNotNull(jenkins);
        JenkinsOpenTelemetryPluginConfiguration.get().setOmitPipelineStepSpans(true);

        DefaultStepHandler handler = new DefaultStepHandler();

        boolean canCreate = handler.canCreateSpanBuilder(mock(FlowNode.class), mock(WorkflowRun.class));

        assertFalse(canCreate);
    }

    @Test
    void omitPipelineStepSpans_disabled_allowsStepSpanCreation(JenkinsRule jenkins) {
        assertNotNull(jenkins);
        JenkinsOpenTelemetryPluginConfiguration.get().setOmitPipelineStepSpans(false);

        DefaultStepHandler handler = new DefaultStepHandler();

        boolean canCreate = handler.canCreateSpanBuilder(mock(FlowNode.class), mock(WorkflowRun.class));

        assertTrue(canCreate);
    }
}
