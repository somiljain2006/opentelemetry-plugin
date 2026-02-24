package io.jenkins.plugins.opentelemetry.job.step;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import io.jenkins.plugins.opentelemetry.JenkinsOpenTelemetryPluginConfiguration;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class DefaultStepHandlerTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    @After
    public void resetConfig() {
        // avoid leaking config between tests
        JenkinsOpenTelemetryPluginConfiguration.get().setOmitPipelineStepSpans(false);
    }

    @Test
    public void omitPipelineStepSpans_disablesStepSpanCreation() {
        JenkinsOpenTelemetryPluginConfiguration.get().setOmitPipelineStepSpans(true);

        DefaultStepHandler handler = new DefaultStepHandler();

        boolean canCreate = handler.canCreateSpanBuilder(mock(FlowNode.class), mock(WorkflowRun.class));

        assertFalse(canCreate);
    }

    @Test
    public void omitPipelineStepSpans_disabled_allowsStepSpanCreation() {
        JenkinsOpenTelemetryPluginConfiguration.get().setOmitPipelineStepSpans(false);

        DefaultStepHandler handler = new DefaultStepHandler();

        boolean canCreate = handler.canCreateSpanBuilder(mock(FlowNode.class), mock(WorkflowRun.class));

        assertTrue(canCreate);
    }
}
