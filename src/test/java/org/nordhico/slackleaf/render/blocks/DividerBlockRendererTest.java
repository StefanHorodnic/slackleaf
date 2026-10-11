package org.nordhico.slackleaf.render.blocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.nordhico.slackleaf.SlackLeafTemplateRenderer;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DividerBlockRendererTest {
    @Test
    void rendersDividerBlock() throws Exception {
        var renderer = new SlackLeafTemplateRenderer();
        var payload = new ObjectMapper().readValue(renderer.render("divider", Map.of()), Map.class);
        var blocks = (List<Map<String, Object>>) payload.get("blocks");

        assertEquals("divider", blocks.get(0).get("type"));
    }
}
