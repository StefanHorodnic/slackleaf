package org.nordhico.slackleaf.render.blocks;

import org.jsoup.nodes.Element;
import org.nordhico.slackleaf.render.SlackBlockRenderer;
import org.nordhico.slackleaf.render.SlackRenderContext;

import java.util.Map;

public class ImageBlockRenderer implements SlackBlockRenderer {
    @Override
    public String tagName() {
        return "slack-image";
    }

    @Override
    public Map<String, Object> render(Element element, SlackRenderContext context) {
        var block = context.block("image");
        block.put("image_url", context.required(element, "src"));
        block.put("alt_text", context.required(element, "alt"));
        if (element.hasAttr("title")) {
            block.put(
                    "title",
                    context.textObject(
                            element.attr("title"),
                            "plain_text"
                    )
            );
        }
        return block;
    }
}
