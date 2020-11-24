package com.android.jetpack.model;

import java.util.Objects;

public class Ugc {
    /**
     * likeCount : 153
     * shareCount : 0
     * commentCount : 4454
     * hasFavorite : false
     * hasLiked : true
     * hasdiss:false
     */

    public int likeCount;
    public int shareCount;
    public int commentCount;
    public boolean hasFavorite;
    public boolean hasLiked;
    public boolean hasdiss;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ugc ugc = (Ugc) o;
        return likeCount == ugc.likeCount &&
                shareCount == ugc.shareCount &&
                commentCount == ugc.commentCount &&
                hasFavorite == ugc.hasFavorite &&
                hasLiked == ugc.hasLiked &&
                hasdiss == ugc.hasdiss;
    }

    @Override
    public int hashCode() {
        return Objects.hash(likeCount, shareCount, commentCount, hasFavorite, hasLiked, hasdiss);
    }
}
