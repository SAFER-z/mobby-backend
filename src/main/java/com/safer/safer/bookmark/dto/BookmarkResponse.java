package com.safer.safer.bookmark.dto;

import com.safer.safer.bookmark.domain.ResourceType;

public interface BookmarkResponse {
    String getPlaceId();
    ResourceType getResource();
}
