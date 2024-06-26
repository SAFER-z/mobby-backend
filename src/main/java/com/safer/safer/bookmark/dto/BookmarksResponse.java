package com.safer.safer.bookmark.dto;

import java.util.List;

public record BookmarksResponse(
        List<BookmarkResponse> bookmarks
) {
    public static BookmarksResponse of(List<BookmarkResponse> bookmarks) {
        return new BookmarksResponse(bookmarks);
    }
}
