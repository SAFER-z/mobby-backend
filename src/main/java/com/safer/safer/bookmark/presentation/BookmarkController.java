package com.safer.safer.bookmark.presentation;

import com.safer.safer.auth.dto.UserInfo;
import com.safer.safer.auth.presentation.Auth;
import com.safer.safer.bookmark.application.BookmarkService;
import com.safer.safer.bookmark.domain.ResourceType;
import com.safer.safer.bookmark.dto.BookmarksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping
    public ResponseEntity<BookmarksResponse> findBookmarks(@Auth UserInfo userInfo) {
        return ResponseEntity.ok(bookmarkService.findBookmarks(userInfo));
    }

    @PostMapping
    public ResponseEntity<Void> createBookmark(
            @Auth UserInfo userInfo,
            @RequestParam String placeId,
            @RequestParam ResourceType resource
    ) {
        bookmarkService.saveBookmark(userInfo, placeId, resource);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBookmark(
            @Auth UserInfo userInfo,
            @RequestParam String placeId,
            @RequestParam ResourceType resource
    ) {
        bookmarkService.deleteBookmark(userInfo, placeId, resource);
        return ResponseEntity.noContent().build();
    }
}
