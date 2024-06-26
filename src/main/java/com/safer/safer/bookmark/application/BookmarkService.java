package com.safer.safer.bookmark.application;

import com.safer.safer.auth.dto.UserInfo;
import com.safer.safer.bookmark.domain.Bookmark;
import com.safer.safer.bookmark.domain.ResourceType;
import com.safer.safer.bookmark.domain.repository.BookmarkRepository;
import com.safer.safer.bookmark.dto.BookmarksResponse;
import com.safer.safer.common.exception.BadRequestException;
import com.safer.safer.common.exception.ForbiddenException;
import com.safer.safer.common.exception.NoSuchElementException;
import com.safer.safer.user.domain.User;
import com.safer.safer.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.safer.safer.common.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    public BookmarksResponse findBookmarks(UserInfo userInfo) {
        Long userId = userInfo.userId();
        if(!userRepository.existsById(String.valueOf(userId))) {
            throw new NoSuchElementException(NO_SUCH_USER_ACCOUNT);
        }

        return BookmarksResponse.of(bookmarkRepository.findByUserId(userId));
    }

    @Transactional
    public void saveBookmark(UserInfo userInfo, String placeId, ResourceType resource) {
        Long userId = userInfo.userId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_USER_ACCOUNT));

        validateBookmarkDuplication(user, placeId, resource);

        Bookmark bookmark = Bookmark.of(placeId, resource, user);
        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void deleteBookmark(UserInfo userInfo, String placeId, ResourceType resource) {
        Long userId = userInfo.userId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_USER_ACCOUNT));

        validateBookmarkDeletion(user, placeId, resource);
        bookmarkRepository.deleteByUserAndPlaceIdAndResource(user, placeId, resource);
    }

    private void validateBookmarkDuplication(User user, String placeId, ResourceType resource) {
        if(isExistingBookmark(user, placeId, resource)) {
            throw new BadRequestException(DUPLICATE_BOOKMARK_REQUEST);
        }
    }

    private void validateBookmarkDeletion(User user, String placeId, ResourceType resource) {
        if(isExistingBookmark(user, placeId, resource)) {
            return;
        }
        throw new ForbiddenException(BOOKMARK_DELETION_FORBIDDEN);
    }

    public boolean isExistingBookmark(User user, String placeId, ResourceType resource) {
        return bookmarkRepository.existsByUserAndAndPlaceIdAndResource(
                user,
                placeId,
                resource
        );
    }
}
