package org.instagramFollowFeature.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Post {
    private static int nextId = 1;

    int postId;
    String userId;
    String caption;
    long createdAt;                              // a counter is enough to order posts
    Set<String> likedBy = new HashSet<>();       // a set so one user can like only once
    List<Comment> comments = new ArrayList<>();

    public Post(String userId, String caption) {
        this.postId = nextId;
        this.createdAt = nextId;
        nextId++;
        this.userId = userId;
        this.caption = caption;
    }

    public int getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCaption() {
        return caption;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public Set<String> getLikedBy() {
        return likedBy;
    }

    public List<Comment> getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "Post " + postId + " by " + userId + ": \"" + caption + "\" (" + likedBy.size() + " likes, "
                + comments.size() + " comments)";
    }
}
