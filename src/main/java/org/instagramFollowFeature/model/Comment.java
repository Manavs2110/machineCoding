package org.instagramFollowFeature.model;

public class Comment {
    private static int nextId = 1;

    int commentId;
    String userId;
    String text;

    public Comment(String userId, String text) {
        this.commentId = nextId++;
        this.userId = userId;
        this.text = text;
    }

    public int getCommentId() {
        return commentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return userId + ": " + text;
    }
}
