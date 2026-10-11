package org.nordhico.slackleaf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.nordhico.slackleaf.render.SlackBlockRendererRegistry;
import org.nordhico.slackleaf.render.SlackRenderContext;
import org.nordhico.slackleaf.render.blocks.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.JsonNodeException;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SlackLeafTemplateRenderer {
    private final TemplateEngine templateEngine;
    private final ObjectMapper objectMapper;
    private final SlackBlockRendererRegistry registry;
    private final SlackRenderContext renderContext;

    public SlackLeafTemplateRenderer() {
        this(defaultTemplateEngine(), new JsonMapper(), defaultRegistry());
    }

    public SlackLeafTemplateRenderer(TemplateEngine templateEngine, ObjectMapper objectMapper, SlackBlockRendererRegistry registry) {
        this.templateEngine = Objects.requireNonNull(templateEngine);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.registry = Objects.requireNonNull(registry);
        this.renderContext = new SlackRenderContext();
        this.renderContext.setRegistry(this.registry);
    }

    public String render(String templateName, Map<String, ?> variables) {
        Objects.requireNonNull(templateName, "templateName");
        var context = new Context();
        if (variables != null) {
            variables.forEach(context::setVariable);
        }
        try {
            var html = templateEngine.process(templateName, context);
            var document = Jsoup.parseBodyFragment(html);
            var root = document.selectFirst("slack-message");
            if (root == null) {
                throw new SlackLeafTemplateException(
                        "Template must contain a <slack-message> root."
                );
            }
            List<Map<String, Object>> blocks = root.children().stream()
                    .filter(child -> child.normalName().startsWith("slack-"))
                    .map(this::renderBlock)
                    .toList();

            Map<String, Object> payload = new java.util.LinkedHashMap<>();
            payload.put("blocks", blocks);
            if (root.hasAttr("text") && !root.attr("text").isBlank()) {
                payload.put("text", root.attr("text"));
            }
            return objectMapper.writeValueAsString(payload);
        } catch (SlackLeafTemplateException exception) {
            throw exception;
        } catch (JsonNodeException exception) {
            throw new SlackLeafTemplateException("Failed to serialize Slack message.", exception);
        } catch (RuntimeException exception) {
            throw new SlackLeafTemplateException("Failed to render Slackleaf template: " + templateName, exception);
        }
    }

    private Map<String, Object> renderBlock(Element element) {
        return registry.get(element.normalName()).render(element, renderContext);
    }

    private static SlackBlockRendererRegistry defaultRegistry() {
        return new SlackBlockRendererRegistry(List.of(
                new HeaderBlockRenderer(),
                new SectionBlockRenderer(),
                new DividerBlockRenderer(),
                new ImageBlockRenderer(),
                new ContextBlockRenderer(),
                new ActionsBlockRenderer(),
                new FileBlockRenderer(),
                new VideoBlockRenderer(),
                new ContainerBlockRenderer(),
                new MarkdownRenderer(),
                new ButtonElementRenderer(),
                new CustomBlockRenderer()
        ));
    }

    private static TemplateEngine defaultTemplateEngine() {
        var resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);

        var engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
        return engine;
    }
}
