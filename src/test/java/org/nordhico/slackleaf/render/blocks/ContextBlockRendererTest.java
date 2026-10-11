package org.nordhico.slackleaf.render.blocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.nordhico.slackleaf.SlackLeafTemplateRenderer;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ContextBlockRendererTest {
    @Test
    void rendersContextBlock() throws Exception {
        var renderer = new SlackLeafTemplateRenderer();
        var payload = new ObjectMapper().readValue(renderer.render("context", Map.of()), Map.class);
        var blocks = (List<Map<String, Object>>) payload.get("blocks");
        var block = blocks.get(0);

        assertEquals("context", block.get("type"));
        var elements = (List<Map<String, Object>>) block.get("elements");
        assertNotNull(elements);
        assertEquals(2, elements.size());
        assertEquals("mrkdwn", elements.get(0).get("type"));
        assertEquals("Updated by @alice", elements.get(0).get("text"));
        assertEquals("image", elements.get(1).get("type"));
    }
}
