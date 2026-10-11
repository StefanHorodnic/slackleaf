package org.nordhico.slackleaf.render.blocks;

import org.junit.jupiter.api.Test;
import org.nordhico.slackleaf.SlackLeafTemplateRenderer;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SectionBlockRendererTest {
    @Test
    void rendersSectionBlock() throws Exception {
        var renderer = new SlackLeafTemplateRenderer();
        var payload = new ObjectMapper().readValue(renderer.render("section", Map.of()), Map.class);
        var blocks = (List<Map<String, Object>>) payload.get("blocks");
        var block = blocks.get(0);

        assertEquals("section", block.get("type"));
        assertEquals("mrkdwn", ((Map<String, Object>) block.get("text")).get("type"));
        assertEquals("Hello *world*", ((Map<String, Object>) block.get("text")).get("text"));
    }
}
