package org.nordhico.slackleaf.render.blocks;

import org.jsoup.nodes.Element;
import org.nordhico.slackleaf.render.SlackBlockRenderer;
import org.nordhico.slackleaf.render.SlackRenderContext;

import java.util.Map;

public class ButtonElementRenderer implements SlackBlockRenderer {
    @Override
    public String tagName() {
        return "slack-button";
    }

    @Override
    public Map<String, Object> render(Element element, SlackRenderContext context) {
        var button = context.block("button");
        button.put(
                "text",
                context.textObject(
                        context.required(element, "text"),
                        "plain_text"
                )
        );
        button.put("action_id", context.required(element, "action-id"));

        if (element.hasAttr("value")) {
            button.put("value", element.attr("value"));
        }

        if (element.hasAttr("url")) {
            button.put("url", element.attr("url"));
        }

        if (element.hasAttr("style")) {
            button.put("style", element.attr("style"));
        }

        return button;
    }
}
