package org.nordhico.slackleaf.render.blocks;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.nordhico.slackleaf.render.SlackBlockRenderer;
import org.nordhico.slackleaf.render.SlackRenderContext;

import java.util.Map;

public class CustomBlockRenderer implements SlackBlockRenderer {
    @Override
    public String tagName() {
        return "slack-block";
    }

    @Override
    public Map<String, Object> render(Element element, SlackRenderContext context) {
        var block = context.block(context.required(element, "type"));
        for (var attribute : element.attributes()) {
            var name = attribute.getKey();
            if (name.equals("type") || name.startsWith("th:")) {
                continue;
            }
            block.put(name.replace('-', '_'), attribute.getValue());
        }
        if (!element.text().isBlank()) {
            block.put("text", context.textObject(element.text(), "mrkdwn"));
        }
        return block;
    }
}
