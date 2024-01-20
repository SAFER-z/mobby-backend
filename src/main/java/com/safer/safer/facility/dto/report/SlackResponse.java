package com.safer.safer.facility.dto.report;

public record SlackResponse(
        String text
) {
    public static SlackResponse of(String text) {
        return new SlackResponse(text);
    }
}
