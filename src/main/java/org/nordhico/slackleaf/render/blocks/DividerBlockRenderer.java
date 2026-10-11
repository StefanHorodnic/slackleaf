package org.nordhico.slackleaf.render.blocks;

import org.jsoup.nodes.Element;
import org.nordhico.slackleaf.render.SlackBlockRenderer;
import org.nordhico.slackleaf.render.SlackRenderContext;

import java.util.Map;

public class DividerBlockRenderer implements SlackBlockRenderer {
    @Override
    public String tagName() {
        return "slack-divider";
    }

    @Override
    public Map<String, Object> render(Element element, SlackRenderContext context) {
        return context.block("divider");
    }
}
