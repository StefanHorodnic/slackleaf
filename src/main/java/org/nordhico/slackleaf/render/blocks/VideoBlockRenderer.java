package org.nordhico.slackleaf.render.blocks;

import org.jsoup.nodes.Element;
import org.nordhico.slackleaf.render.SlackBlockRenderer;
import org.nordhico.slackleaf.render.SlackRenderContext;

import java.util.Map;

public class VideoBlockRenderer implements SlackBlockRenderer {
    @Override
    public String tagName() {
        return "slack-video";
    }

    @Override
    public Map<String, Object> render(Element element, SlackRenderContext context) {
        var block = context.block("video");
        block.put("video_url", context.required(element, "src"));
        block.put("alt_text", context.required(element, "alt"));
        return block;
    }
}
