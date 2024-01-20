package com.safer.safer.facility.dto.report;

import java.util.Map;

public record SlackMessage(
        String operation,
        Long userId
) {
    private final static String ACCEPT = "수락";
    private final static String DENY = "거절";

    public static SlackMessage from(Map<String,String> message) {
        String[] text = message.get("text").split(" ");

        return new SlackMessage(
                text[0],
                Long.parseLong(text[1])
        );
    }

    public boolean isValid() {
        return operation.equals(ACCEPT) || operation.equals(DENY);
    }

    public boolean isAccept() {
        return operation.equals(ACCEPT);
    }
}
