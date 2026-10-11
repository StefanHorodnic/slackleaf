package org.nordhico.slackleaf.render.blocks;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.nordhico.slackleaf.SlackLeafTemplateRenderer;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ActionsBlockRendererTest {
    @Test
    void renderActionsBlockUsesContextBasedChildRendering() throws Exception {
        var renderer = new SlackLeafTemplateRenderer();
        var payload = new ObjectMapper().readValue(renderer.render("actions", Map.of()), Map.class);

        var blocks = (List<Map<String, Object>>) payload.get("blocks");
        assertNotNull(blocks);
        assertEquals(1, blocks.size());

        var actions = blocks.get(0);
        assertEquals("actions", actions.get("type"));

        var elements = (List<Map<String, Object>>) actions.get("elements");
        assertNotNull(elements);
        assertEquals(1, elements.size());

        var button = elements.get(0);
        assertEquals("button", button.get("type"));
        assertEquals("view", button.get("action_id"));
        assertEquals("Open", ((Map<String, Object>) button.get("text")).get("text"));
    }
}
