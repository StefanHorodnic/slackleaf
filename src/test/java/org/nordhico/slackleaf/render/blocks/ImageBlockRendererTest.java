package org.nordhico.slackleaf.render.blocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.nordhico.slackleaf.SlackLeafTemplateRenderer;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageBlockRendererTest {
    @Test
    void rendersImageBlock() throws Exception {
        var renderer = new SlackLeafTemplateRenderer();
        var payload = new ObjectMapper().readValue(renderer.render("image", Map.of()), Map.class);
        var blocks = (List<Map<String, Object>>) payload.get("blocks");
        var block = blocks.get(0);

        assertEquals("image", block.get("type"));
        assertEquals("https://example.com/diagram.jpg", block.get("image_url"));
        assertEquals("Diagram alt", block.get("alt_text"));
        assertEquals("Overview", ((Map<String, Object>) block.get("title")).get("text"));
    }
}
