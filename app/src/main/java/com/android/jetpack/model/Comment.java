package com.android.jetpack.model;

import java.util.Objects;

public class Comment {
    public static final int COMMENT_TYPE_VIDEO = 3;
    public static final int COMMENT_TYPE_IMAGE_TEXT = 2;
    /**
     * id : 784
     * itemId : 6739143063064549000
     * commentId : 6739212214408380000
     * userId : 65200808093
     * commentType : 1
     * createTime : 1569095152
     * commentCount : 4454
     * likeCount : 152
     * commentText : 看见没。比甜蜜暴击好看一万倍！
     * imageUrl : null
     * videoUrl : null
     * width : 0
     * height : 0
     * hasLiked : false
     * author : {"id":978,"userId":65200808093,"name":"带鱼裹上面包糠","avatar":"https://sf1-nhcdn-tos.pstatp.com/obj/tos-cn-i-0000/9041325b8fd44dd09fd41d5f2bd379bd","description":null,"likeCount":0,"topCommentCount":0,"followCount":0,"followerCount":0,"qqOpenId":null,"expires_time":0,"score":0,"historyCount":0,"commentCount":0,"favoriteCount":0,"feedCount":0,"hasFollow":false}
     * ugc : {"likeCount":153,"shareCount":0,"commentCount":4454,"hasFavorite":false,"hasLiked":true}
     */

    public int id;
    public long itemId;
    public long commentId;
    public long userId;
    public int commentType;
    public long createTime;
    public int commentCount;
    public int likeCount;
    public String commentText;
    public String imageUrl;
    public String videoUrl;
    public int width;
    public int height;
    public boolean hasLiked;
    public User author;
    public Ugc ugc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id &&
                itemId == comment.itemId &&
                commentId == comment.commentId &&
                userId == comment.userId &&
                commentType == comment.commentType &&
                createTime == comment.createTime &&
                commentCount == comment.commentCount &&
                likeCount == comment.likeCount &&
                width == comment.width &&
                height == comment.height &&
                hasLiked == comment.hasLiked &&
                Objects.equals(commentText, comment.commentText) &&
                Objects.equals(imageUrl, comment.imageUrl) &&
                Objects.equals(videoUrl, comment.videoUrl) &&
                Objects.equals(author, comment.author) &&
                Objects.equals(ugc, comment.ugc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId, commentId, userId, commentType, createTime, commentCount, likeCount, commentText, imageUrl, videoUrl, width, height, hasLiked, author, ugc);
    }
}
