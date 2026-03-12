/*
 * Copyright The Original Author or Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.jenkins.plugins.opentelemetry.backend;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class GrafanaBackendTest {

    @Test
    public void testTraceUrl() {
        GrafanaBackend grafanaBackend = new GrafanaBackend();
        grafanaBackend.setGrafanaBaseUrl("https://cleclerc.grafana.net");
        grafanaBackend.setGrafanaOrgId("1");
        grafanaBackend.setTempoDataSourceIdentifier("grafanacloud-traces");
        grafanaBackend.setTempoDataSourceUid("my-awesome-custom-uid");
        grafanaBackend.setTempoQueryType("traceql");

        LocalDateTime buildTime =
                LocalDateTime.parse("2023-02-05 23:31:52.610", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        Map<String, Object> bindings = new HashMap<>();
        bindings.put("serviceName", "jenkins");
        bindings.put("rootSpanName", "BUILD my-app");
        bindings.put("traceId", "f464e1f32444443d3fc00fdb19e5c124");
        bindings.put("spanId", "00799ea60984f33f");
        bindings.put("startTime", buildTime);

        String actualUrl = grafanaBackend.getTraceVisualisationUrl(bindings);
        System.out.println(actualUrl);

        assertTrue(Objects.requireNonNull(actualUrl).contains("schemaVersion=1"), "URL must contain schemaVersion=1");
        assertTrue(actualUrl.contains("panes="), "URL must use 'panes' instead of 'left'");
        assertTrue(actualUrl.contains("my-awesome-custom-uid"), "URL must contain the new Tempo UID");
        assertTrue(actualUrl.contains("f464e1f32444443d3fc00fdb19e5c124"), "URL must contain the exact traceId");
    }

    @Test
    public void testTraceUrlFallbackToIdentifier() {
        GrafanaBackend grafanaBackend = new GrafanaBackend();
        grafanaBackend.setGrafanaBaseUrl("https://cleclerc.grafana.net");
        grafanaBackend.setGrafanaOrgId("1");
        grafanaBackend.setTempoDataSourceIdentifier("grafanacloud-traces");
        grafanaBackend.setTempoQueryType("traceql");

        LocalDateTime buildTime =
                LocalDateTime.parse("2023-02-05 23:31:52.610", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        Map<String, Object> bindings = new HashMap<>();
        bindings.put("serviceName", "jenkins");
        bindings.put("rootSpanName", "BUILD my-app");
        bindings.put("traceId", "f464e1f32444443d3fc00fdb19e5c124");
        bindings.put("spanId", "00799ea60984f33f");
        bindings.put("startTime", buildTime);

        String actualUrl = grafanaBackend.getTraceVisualisationUrl(bindings);

        assertTrue(Objects.requireNonNull(actualUrl).contains("schemaVersion=1"), "URL must contain schemaVersion=1");
        assertTrue(actualUrl.contains("panes="), "URL must use 'panes' instead of 'left'");
        assertTrue(
                actualUrl.contains("grafanacloud-traces"),
                "URL must fall back to the tempoDataSourceIdentifier when UID is missing");
        assertTrue(actualUrl.contains("f464e1f32444443d3fc00fdb19e5c124"), "URL must contain the exact traceId");
    }
}
