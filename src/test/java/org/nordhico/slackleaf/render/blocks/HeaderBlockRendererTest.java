package org.nordhico.slackleaf.render.blocks;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.nordhico.slackleaf.SlackLeafTemplateRenderer;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeaderBlockRendererTest {
    @Test
    void rendersHeaderBlock() throws Exception {
        var renderer = new SlackLeafTemplateRenderer();
        var payload = new ObjectMapper().readValue(renderer.render("header", Map.of()), Map.class);
        var blocks = (List<Map<String, Object>>) payload.get("blocks");

        assertEquals("header", blocks.get(0).get("type"));
        assertEquals("Alert", ((Map<String, Object>) blocks.get(0).get("text")).get("text"));
    }
}
