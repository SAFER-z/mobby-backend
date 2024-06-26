package com.safer.safer.bookmark.domain.repository;

import com.safer.safer.bookmark.domain.Bookmark;
import com.safer.safer.bookmark.domain.ResourceType;
import com.safer.safer.bookmark.dto.BookmarkResponse;
import com.safer.safer.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {

    @Query(value = "select b.place_id as placeId, b.resource from Bookmark b where b.user_id = :userId order by b.created_at desc", nativeQuery = true)
    List<BookmarkResponse> findByUserId(@Param("userId") Long userId);

    boolean existsByUserAndAndPlaceIdAndResource(User user, String placeId, ResourceType resource);

    void deleteByUserAndPlaceIdAndResource(User user, String placeId, ResourceType resource);
}
