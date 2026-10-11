package org.nordhico.slackleaf.render.blocks;

import org.jsoup.nodes.Element;
import org.nordhico.slackleaf.SlackLeafTemplateException;
import org.nordhico.slackleaf.render.SlackBlockRenderer;
import org.nordhico.slackleaf.render.SlackRenderContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContextBlockRenderer implements SlackBlockRenderer {
    @Override
    public String tagName() {
        return "slack-context";
    }

    @Override
    public Map<String, Object> render(Element element, SlackRenderContext context) {
        var block = context.block("context");
        List<Object> elements = new ArrayList<>();
        for (var child : element.children()) {
            switch (child.normalName()) {
                case "slack-markdown" ->
                        elements.add(
                                context.textObject(
                                        child.wholeText(),
                                        "mrkdwn"
                                )
                        );
                case "slack-plain-text" ->
                        elements.add(
                                context.textObject(
                                        child.text(),
                                        "plain_text"
                                )
                        );
                case "slack-image" -> {
                    var image =
                            context.block("image");
                    image.put(
                            "image_url",
                            context.required(child, "src")
                    );
                    image.put(
                            "alt_text",
                            context.required(child, "alt")
                    );
                    elements.add(image);
                }
                default -> throw new SlackLeafTemplateException("Unsupported <slack-context> child: <" + child.normalName() + ">.");
            }
        }
        if (elements.isEmpty()) {
            throw new SlackLeafTemplateException("<slack-context> must contain at least one element.");
        }
        block.put("elements", elements);
        return block;
    }
}
