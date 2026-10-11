package org.nordhico.slackleaf.render.blocks;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.nordhico.slackleaf.SlackLeafTemplateRenderer;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomBlockRendererTest {
    @Test
    void rendersCustomBlock() throws Exception {
        var renderer = new SlackLeafTemplateRenderer();
        var payload = new ObjectMapper().readValue(renderer.render("custom", Map.of()), Map.class);
        var blocks = (List<Map<String, Object>>) payload.get("blocks");
        var block = blocks.get(0);

        assertEquals("custom", block.get("type"));
        assertEquals("custom-123", block.get("data_id"));
        assertEquals("Custom text", ((Map<String, Object>) block.get("text")).get("text"));
    }
}
