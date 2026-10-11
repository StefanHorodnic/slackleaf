package org.nordhico.slackleaf.render.blocks;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.nordhico.slackleaf.SlackLeafTemplateRenderer;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VideoBlockRendererTest {
    @Test
    void rendersVideoBlock() throws Exception {
        var renderer = new SlackLeafTemplateRenderer();
        var payload = new ObjectMapper().readValue(renderer.render("video", Map.of()), Map.class);
        var blocks = (List<Map<String, Object>>) payload.get("blocks");
        var block = blocks.get(0);

        assertEquals("video", block.get("type"));
        assertEquals("https://example.com/demo.mp4", block.get("video_url"));
        assertEquals("Demo video", block.get("alt_text"));
    }
}
