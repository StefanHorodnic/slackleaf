package org.nordhico.slackleaf.render.blocks;

import org.jsoup.nodes.Element;
import org.nordhico.slackleaf.SlackLeafTemplateException;
import org.nordhico.slackleaf.render.SlackBlockRenderer;
import org.nordhico.slackleaf.render.SlackRenderContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContainerBlockRenderer implements SlackBlockRenderer {
    @Override
    public String tagName() {
        return "slack-container";
    }

    @Override
    public Map<String, Object> render(Element element, SlackRenderContext context) {
        var block = context.block("container");

        if (element.hasAttr("title")) {
            block.put("title", context.textObject(element.attr("title"), "plain_text"));
        }

        if (element.hasAttr("subtitle")) {
            block.put("subtitle", context.textObject(element.attr("subtitle"), "plain_text"));
        }

        if (element.hasAttr("block-id")) {
            block.put("block_id", element.attr("block-id"));
        }

        if (element.hasAttr("width")) {
            block.put("width", element.attr("width"));
        }

        if (element.hasAttr("is-collapsible")) {
            block.put("is_collapsible", booleanAttribute(element, "is-collapsible"));
        }

        if (element.hasAttr("default-collapsed")) {
            block.put("default_collapsed", booleanAttribute(element, "default-collapsed"));
        }

        if (element.hasAttr("has-header-divider")) {
            block.put("has_header_divider", booleanAttribute(element, "has-header-divider"));
        }

        List<Object> childBlocks = new ArrayList<>();
        for (var child : element.children()) {
            if (child.normalName().startsWith("slack-")) {
                childBlocks.add(context.renderBlock(child));
            }
        }

        if (childBlocks.isEmpty()) {
            throw new SlackLeafTemplateException("<slack-container> must contain at least one child block.");
        }

        block.put("child_blocks", childBlocks);
        return block;
    }

    private boolean booleanAttribute(Element element, String name) {
        var value = element.attr(name);
        return value == null || value.isBlank() || Boolean.parseBoolean(value);
    }
}
