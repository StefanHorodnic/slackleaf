package org.nordhico.slackleaf.render.blocks;

import org.jsoup.nodes.Element;
import org.nordhico.slackleaf.render.SlackBlockRenderer;
import org.nordhico.slackleaf.render.SlackRenderContext;

import java.util.Map;

public class FileBlockRenderer implements SlackBlockRenderer {
    @Override
    public String tagName() {
        return "slack-file";
    }

    @Override
    public Map<String, Object> render(Element element, SlackRenderContext context) {
        var block = context.block("file");
        block.put("external_id", context.required(element, "external-id"));
        block.put("source", element.hasAttr("source") ? element.attr("source") : "remote");
        return block;
    }
}
