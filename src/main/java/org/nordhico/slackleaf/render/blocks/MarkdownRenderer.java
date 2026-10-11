package org.nordhico.slackleaf.render.blocks;

import org.jsoup.nodes.Element;
import org.nordhico.slackleaf.render.SlackBlockRenderer;
import org.nordhico.slackleaf.render.SlackRenderContext;

import java.util.Map;

public class MarkdownRenderer implements SlackBlockRenderer {
    @Override
    public String tagName() {
        return "slack-markdown";
    }

    @Override
    public Map<String, Object> render(Element element, SlackRenderContext context) {
        var block = context.block("markdown");
        block.put("text", element.wholeText().trim());
        return block;
    }
}
