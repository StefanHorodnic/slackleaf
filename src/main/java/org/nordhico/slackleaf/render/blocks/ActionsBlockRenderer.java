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
        List<Object> buttons = new ArrayList<>();
        for (Element child : element.children()) {
            if (!child.normalName().equals("slack-button")) {
                throw new SlackLeafTemplateException("Unsupported <slack-actions> child: <" + child.normalName() + ">.");
            }
            var button = context.block("button");
            button.put(
                    "text",
                    context.textObject(
                            context.required(child, "text"),
                            "plain_text"
                    )
            );
            button.put(
                    "action_id",
                    context.required(child, "action-id")
            );

            if (child.hasAttr("value")) {
                button.put("value", child.attr("value"));
            }

            if (child.hasAttr("url")) {
                button.put("url", child.attr("url"));
            }

            if (child.hasAttr("style")) {
                button.put("style", child.attr("style"));
            }

            buttons.add(button);
        }

        if (buttons.isEmpty()) {
            throw new SlackLeafTemplateException("<slack-actions> must contain at least one button.");
        }
        block.put("elements", buttons);
        return block;
    }
}
