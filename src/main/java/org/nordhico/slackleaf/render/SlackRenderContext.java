package org.nordhico.slackleaf.render;

import org.jsoup.nodes.Element;
import org.nordhico.slackleaf.SlackLeafTemplateException;

import java.util.LinkedHashMap;
import java.util.Map;

public class SlackRenderContext {
    private SlackBlockRendererRegistry registry;

    public void setRegistry(SlackBlockRendererRegistry registry) {
        this.registry = registry;
    }

    public Map<String, Object> renderBlock(Element element) {
        if (registry == null) {
            throw new IllegalStateException("No Slack block registry configured for render context.");
        }
        return registry.get(element.normalName()).render(element, this);
    }

    public String required(Element element, String attribute) {
        var value = element.attr(attribute);
        if (value == null || value.isBlank()) {
            throw new SlackLeafTemplateException("<" + element.normalName() + "> requires attribute '" + attribute + "'.");
        }
        return value;
    }

    public Map<String, Object> textObject(String text, String type) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", type);
        result.put("text", text);
        return result;
    }

    public Map<String, Object> block(String type) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", type);
        return result;
    }
}
