package org.nordhico.slackleaf.render.blocks;

import org.jsoup.nodes.Element;
import org.nordhico.slackleaf.SlackLeafTemplateException;
import org.nordhico.slackleaf.render.SlackBlockRenderer;
import org.nordhico.slackleaf.render.SlackRenderContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionsBlockRenderer implements SlackBlockRenderer {
    @Override
    public String tagName() {
        return "slack-actions";
    }

    @Override
    public Map<String, Object> render(Element element, SlackRenderContext context) {
        var block = context.block("actions");
        List<Object> elements = new ArrayList<>();
        for (Element child : element.children()) {
            if (!child.normalName().startsWith("slack-")) {
                continue;
            }
            elements.add(context.renderBlock(child));
        }
        if (elements.isEmpty()) {
            throw new SlackLeafTemplateException("<slack-actions> must contain at least one Slack element.");
        }
        block.put("elements", elements);
        return block;
    }
}
