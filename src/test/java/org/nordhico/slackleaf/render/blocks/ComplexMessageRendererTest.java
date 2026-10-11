package org.nordhico.slackleaf.render.blocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.nordhico.slackleaf.SlackLeafTemplateRenderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ComplexMessageRendererTest {
    @Test
    void renderComplexNestedMessageWithContainerAndActions() throws Exception {
        var renderer = new SlackLeafTemplateRenderer();

        Map<String, Object> variables = new HashMap<>();
        variables.put("messageText", "Complex Example");
        variables.put("headerText", "Review queue");
        variables.put("customerLine", "*Customer:* Acme Corp");
        variables.put("summary", "*Status:* 2 records ready for review");
        variables.put("actions", List.of(
                Map.of("id", "approve", "label", "Approve", "style", "primary", "value", "approve"),
                Map.of("id", "review", "label", "Review", "style", "danger", "value", "review")
        ));
        variables.put("updatedBy", "Updated by @alice");
        variables.put("detailRows", List.of("Open → Closed", "In Progress → Reviewed"));
        variables.put("showFooter", true);
        variables.put("footer", "*Next step:* Confirm in the admin panel");

        var payload = new ObjectMapper().readValue(renderer.render("complex", variables), Map.class);

        var blocks = (List<Map<String, Object>>) payload.get("blocks");
        assertNotNull(blocks);
        assertEquals(2, blocks.size());

        assertEquals("header", blocks.get(0).get("type"));
        assertEquals("Review queue", ((Map<String, Object>) blocks.get(0).get("text")).get("text"));

        var outerContainer = blocks.get(1);
        assertEquals("container", outerContainer.get("type"));
        assertEquals("bulk-update", outerContainer.get("block_id"));

        var childBlocks = (List<Map<String, Object>>) outerContainer.get("child_blocks");
        assertNotNull(childBlocks);
        assertEquals(5, childBlocks.size());

        assertEquals("section", childBlocks.get(0).get("type"));

        var actions = childBlocks.get(2);
        assertEquals("actions", actions.get("type"));
        var actionElements = (List<Map<String, Object>>) actions.get("elements");
        assertEquals(2, actionElements.size());
        assertEquals("button", actionElements.get(0).get("type"));
        assertEquals("approve", actionElements.get(0).get("action_id"));
        assertEquals("button", actionElements.get(1).get("type"));
        assertEquals("review", actionElements.get(1).get("action_id"));

        assertEquals("divider", childBlocks.get(3).get("type"));

        var nestedContainer = childBlocks.get(4);
        assertEquals("container", nestedContainer.get("type"));
        assertEquals("summary-panel", nestedContainer.get("block_id"));
        assertEquals("Summary", ((Map<String, Object>) nestedContainer.get("title")).get("text"));

        var nestedChildren = (List<Map<String, Object>>) nestedContainer.get("child_blocks");
        assertEquals(4, nestedChildren.size());
        assertEquals("context", nestedChildren.get(0).get("type"));
        assertEquals("section", nestedChildren.get(1).get("type"));
        assertEquals("section", nestedChildren.get(2).get("type"));
        assertEquals("section", nestedChildren.get(3).get("type"));
    }
}
