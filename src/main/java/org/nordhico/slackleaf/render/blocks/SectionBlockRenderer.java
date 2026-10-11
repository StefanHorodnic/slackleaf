package org.nordhico.slackleaf.render.blocks;

import org.jsoup.nodes.Element;
import org.nordhico.slackleaf.render.SlackBlockRenderer;
import org.nordhico.slackleaf.render.SlackRenderContext;

import java.util.Map;

public class SectionBlockRenderer implements SlackBlockRenderer {
    @Override
    public String tagName() {
        return "slack-section";
    }

    @Override
    public Map<String, Object> render(Element element, SlackRenderContext context) {
        var block = context.block("section");

        var markdown = element.selectFirst("slack-markdown");
        var plainText = element.selectFirst("slack-plain-text");

        if (markdown != null) {
            block.put(
                    "text",
                    context.textObject(markdown.wholeText(), "mrkdwn")
            );
        } else if (plainText != null) {
            block.put(
                    "text",
                    context.textObject(plainText.text(), "plain_text")
            );
        } else {
            block.put(
                    "text",
                    context.textObject(element.text(), "mrkdwn")
            );
        }

        return block;
    }
}
