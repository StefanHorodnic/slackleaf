package org.nordhico.slackleaf.render;

import org.jsoup.nodes.Element;

import java.util.Map;

public interface SlackBlockRenderer {

    /**
     * HTML tag handled by this renderer, e.g. slack-section.
     */
    String tagName();

    /**
     * Convert a rendered SlackLeaf element into a Block Kit object.
     */
    Map<String, Object> render(
            Element element,
            SlackRenderContext context
    );
}