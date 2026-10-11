package org.nordhico.slackleaf;

public class SlackLeafTemplateException extends RuntimeException {
    public SlackLeafTemplateException(String message) {
        super(message);
    }

    public SlackLeafTemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
