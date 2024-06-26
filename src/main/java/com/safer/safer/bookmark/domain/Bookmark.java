package com.safer.safer.bookmark.domain;

import com.safer.safer.common.domain.BaseEntity;
import com.safer.safer.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String placeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceType resource;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Bookmark(String placeId, ResourceType resource, User user) {
        this.placeId = placeId;
        this.resource = resource;
        this.user = user;
    }

    public static Bookmark of(String placeId, ResourceType resource, User user) {
        return new Bookmark(placeId, resource, user);
    }
}
