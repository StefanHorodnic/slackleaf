# SlackLeaf

SlackLeaf is a Thymeleaf-based HTML DSL for building Slack Block Kit payloads without writing raw JSON by hand.

It renders Slack message templates created with custom HTML-like tags into the final Block Kit JSON payload.

## Root tag

Every template must contain a root `<slack-message>` element.

```html
<slack-message text="My message">
  <slack-header>Hello</slack-header>
</slack-message>
```

Supported root attributes:

- `text` — optional fallback message text
- any standard Thymeleaf attributes like `th:text`, `th:if`, `th:each`, etc.

## Custom tags and attributes

The project currently supports these custom tags:

### `slack-header`

Creates a Slack `header` block.

Attributes:

- none required
- text is taken from the element content

Example:

```html
<slack-header>Alert</slack-header>
```

### `slack-section`

Creates a Slack `section` block.

Attributes:

- none required
- content may be plain text or nested Slack text tags

Supported nested tags:

- `slack-markdown`
- `slack-plain-text`

Example:

```html
<slack-section>Hello *world*</slack-section>

<slack-section>
  <slack-markdown>This is *markdown*</slack-markdown>
</slack-section>
```

### `slack-divider`

Creates a Slack `divider` block.

Example:

```html
<slack-divider></slack-divider>
```

### `slack-image`

Creates a Slack `image` block.

Required attributes:

- `src`
- `alt`

Optional attributes:

- `title`

Example:

```html
<slack-image src="https://example.com/diagram.jpg" alt="Diagram alt" title="Overview"></slack-image>
```

### `slack-context`

Creates a Slack `context` block.

Allowed child tags:

- `slack-markdown`
- `slack-plain-text`
- `slack-image`

Example:

```html
<slack-context>
  <slack-markdown>Updated by @alice</slack-markdown>
  <slack-image src="https://example.com/avatar.jpg" alt="avatar"></slack-image>
</slack-context>
```

### `slack-actions`

Creates a Slack `actions` block.

Allowed child tags:

- `slack-button`

Example:

```html
<slack-actions>
  <slack-button action-id="view" text="Open" style="primary" value="details"></slack-button>
</slack-actions>
```

### `slack-button`

Creates a Slack `button` element inside an `actions` block.

Required attributes:

- `action-id`
- `text`

Optional attributes:

- `value`
- `url`
- `style`

Example:

```html
<slack-button action-id="approve" text="Approve" style="primary" value="approve"></slack-button>
```

### `slack-file`

Creates a Slack `file` block.

Required attributes:

- `external-id`

Optional attributes:

- `source` (defaults to `remote`)

Example:

```html
<slack-file external-id="abc123" source="remote"></slack-file>
```

### `slack-video`

Creates a Slack `video` block.

Required attributes:

- `src`
- `alt`

Example:

```html
<slack-video src="https://example.com/demo.mp4" alt="Demo video"></slack-video>
```

### `slack-container`

Creates a Slack `container` block.

Supported attributes:

- `block-id`
- `title`
- `subtitle`
- `width`
- `is-collapsible`
- `default-collapsed`
- `has-header-divider`

Supported children:

- any Slack block tag, including nested `slack-section`, `slack-actions`, `slack-divider`, and other `slack-container` values

Example:

```html
<slack-container
    block-id="bulk-update"
    title="Bulk update"
    subtitle="Review changes before confirming"
    is-collapsible="true">
  <slack-section>*Customer:* Acme Corp</slack-section>
  <slack-actions>
    <slack-button action-id="approve" text="Approve" style="primary" value="approve"></slack-button>
  </slack-actions>
</slack-container>
```

### `slack-markdown`

Creates a Slack `markdown` block when used as a top-level block.

Required content:

- element text is used as the markdown payload

Optional attributes:

- `block-id` is accepted but ignored by Slack, since markdown blocks do not retain `block_id`

Example:

```html
<slack-markdown>
  **Lots of information here!!**
</slack-markdown>
```

When nested inside `slack-section` or `slack-context`, the same tag name is used as a text helper to emit a Slack `mrkdwn` text object.

### `slack-block`

Passes a custom Slack block through directly.

Required attributes:

- `type`

Any other attributes are copied into the final JSON object with their attribute names preserved as-is, except HTML hyphen names are handled as Slack field names where applicable.

Example:

```html
<slack-block type="custom" data-id="custom-123">Custom text</slack-block>
```

## Text helper tags

The following helper tags are supported as children inside `slack-section` and `slack-context`:

### `slack-markdown`

Produces a Slack `mrkdwn` text object when used as a nested child tag. This is the same tag name used for the standalone markdown block.

### `slack-plain-text`

Produces a Slack `plain_text` text object.

## Attribute conventions

HTML uses kebab-case attributes in the template, and SlackLeaf maps them to their Slack JSON names:

- `action-id` -> `action_id`
- `block-id` -> `block_id`
- `external-id` -> `external_id`
- `is-collapsible` -> `is_collapsible`
- `default-collapsed` -> `default_collapsed`
- `has-header-divider` -> `has_header_divider`

This allows templates to stay readable while producing valid Slack Block Kit JSON.

## Thymeleaf support

SlackLeaf supports normal Thymeleaf features, including:

- `th:text`
- `th:attr`
- `th:if`
- `th:each`

Example:

```html
<slack-message th:attr="text=${messageText}">
  <slack-header th:text="${headerText}"></slack-header>

  <slack-actions>
    <slack-button
        th:each="action : ${actions}"
        th:attr="action-id=${action.id}, text=${action.label}, style=${action.style}, value=${action.value}">
    </slack-button>
  </slack-actions>
</slack-message>
```

## Rendering

```java
var renderer = new SlackLeafTemplateRenderer();
String json = renderer.render("hello", Map.of());
```

The renderer reads the Thymeleaf template, converts the SlackLeaf tags into Block Kit objects, and returns the final JSON payload.

## Summary

The library currently supports the custom tag set needed for real Slack message composition:

- message wrapper
- header, section, divider, image, context, actions, button
- file, video, container, and raw custom block passthrough
- nested child block rendering
- Thymeleaf expressions and loops
