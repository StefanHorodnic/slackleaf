package org.nordhico.slackleaf.render.blocks;

import org.jsoup.nodes.Element;
import org.nordhico.slackleaf.render.SlackBlockRenderer;
import org.nordhico.slackleaf.render.SlackRenderContext;

import java.util.Map;

public class HeaderBlockRenderer implements SlackBlockRenderer {
    @Override
    public String tagName() {
        return "slack-header";
    }

    @Override
    public Map<String, Object> render(Element element, SlackRenderContext context) {
        var block = context.block("header");
        block.put(
                "text",
                context.textObject(element.text(), "plain_text")
        );
        return block;
    }
}
