package org.nordhico.slackleaf.render.blocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.nordhico.slackleaf.SlackLeafTemplateRenderer;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ContainerBlockRendererTest {
    @Test
    void renderContainerBlockSupportsNestedChildrenAndTextMetadata() throws Exception {
        var renderer = new SlackLeafTemplateRenderer();
        var payload = new ObjectMapper().readValue(renderer.render("container", Map.of()), Map.class);

        var blocks = (List<Map<String, Object>>) payload.get("blocks");
        assertNotNull(blocks);
        assertEquals(1, blocks.size());

        var container = blocks.get(0);
        assertEquals("container", container.get("type"));
        assertEquals("container-block", container.get("block_id"));

        var title = (Map<String, Object>) container.get("title");
        assertEquals("plain_text", title.get("type"));
        assertEquals("Bulk update", title.get("text"));

        var subtitle = (Map<String, Object>) container.get("subtitle");
        assertEquals("plain_text", subtitle.get("type"));
        assertEquals("Review changes before confirming", subtitle.get("text"));

        assertEquals(Boolean.TRUE, container.get("is_collapsible"));

        var childBlocks = (List<Map<String, Object>>) container.get("child_blocks");
        assertNotNull(childBlocks);
        assertEquals(2, childBlocks.size());
        assertEquals("section", childBlocks.get(0).get("type"));
        assertEquals("divider", childBlocks.get(1).get("type"));
    }
}
