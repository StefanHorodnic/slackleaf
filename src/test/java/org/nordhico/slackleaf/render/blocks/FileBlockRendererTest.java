package org.nordhico.slackleaf.render.blocks;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.nordhico.slackleaf.SlackLeafTemplateRenderer;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBlockRendererTest {
    @Test
    void rendersFileBlock() throws Exception {
        var renderer = new SlackLeafTemplateRenderer();
        var payload = new ObjectMapper().readValue(renderer.render("file", Map.of()), Map.class);
        var blocks = (List<Map<String, Object>>) payload.get("blocks");
        var block = blocks.get(0);

        assertEquals("file", block.get("type"));
        assertEquals("abc123", block.get("external_id"));
        assertEquals("remote", block.get("source"));
    }
}
