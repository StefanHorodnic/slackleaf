package org.nordhico.slackleaf.render;

import org.nordhico.slackleaf.SlackLeafTemplateException;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class SlackBlockRendererRegistry {
    private final Map<String, SlackBlockRenderer> renderers;

    public SlackBlockRendererRegistry(Collection<SlackBlockRenderer> renderers) {
        Map<String, SlackBlockRenderer> registered = new LinkedHashMap<>();
        for (SlackBlockRenderer renderer : renderers) {
            var previous = registered.putIfAbsent(renderer.tagName(), renderer);
            if (previous != null) {
                throw new IllegalArgumentException("Duplicate Slackleaf renderer for tag: " + renderer.tagName());
            }
        }
        this.renderers = Map.copyOf(registered);
    }

    public SlackBlockRenderer get(String tagName) {
        var renderer = renderers.get(tagName);
        if (renderer == null) {
            throw new SlackLeafTemplateException("No Slackleaf renderer registered for <" + tagName + ">.");
        }
        return renderer;
    }

    public boolean supports(String tagName) {
        return renderers.containsKey(tagName);
    }
}
