package org.instagramFollowFeature.service;

import org.instagramFollowFeature.model.Comment;
import org.instagramFollowFeature.model.Post;
import org.instagramFollowFeature.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstagramService {
    Map<String, User> users = new HashMap<>();
    Map<Integer, Post> posts = new HashMap<>();

    public User createUser(String userId, String name) {
        if (users.containsKey(userId)) throw new RuntimeException("User already exists: " + userId);
        User user = new User(userId, name);
        users.put(userId, user);
        return user;
    }

    // ---------- follow ----------

    public void follow(String followerId, String followeeId) {
        if (followerId.equals(followeeId)) throw new RuntimeException("Cannot follow yourself");
        User follower = getUser(followerId);
        User followee = getUser(followeeId);
        follower.getFollowing().add(followeeId);
        followee.getFollowers().add(followerId);
    }

    public void unfollow(String followerId, String followeeId) {
        User follower = getUser(followerId);
        User followee = getUser(followeeId);
        follower.getFollowing().remove(followeeId);
        followee.getFollowers().remove(followerId);
    }

    // ---------- post ----------

    public Post createPost(String userId, String caption) {
        getUser(userId);   // make sure the user exists
        Post post = new Post(userId, caption);
        posts.put(post.getPostId(), post);
        return post;
    }

    public void deletePost(String userId, int postId) {
        Post post = getPost(postId);
        if (!post.getUserId().equals(userId)) throw new RuntimeException("Only the owner can delete a post");
        posts.remove(postId);
    }

    // ---------- like ----------

    public void likePost(String userId, int postId) {
        getUser(userId);
        getPost(postId).getLikedBy().add(userId);   // liking twice does nothing
    }

    public void unlikePost(String userId, int postId) {
        getPost(postId).getLikedBy().remove(userId);
    }

    // ---------- comment ----------

    public Comment commentOnPost(String userId, int postId, String text) {
        getUser(userId);
        if (text == null || text.isBlank()) throw new RuntimeException("Comment cannot be empty");
        Comment comment = new Comment(userId, text);
        getPost(postId).getComments().add(comment);
        return comment;
    }

    // ---------- feed ----------

    // Posts from everyone I follow, newest first
    public List<Post> getFeed(String userId) {
        User user = getUser(userId);
        List<Post> feed = new ArrayList<>();
        for (Post post : posts.values()) {
            if (user.getFollowing().contains(post.getUserId())) {
                feed.add(post);
            }
        }
        feed.sort((first, second) -> Long.compare(second.getCreatedAt(), first.getCreatedAt()));
        return feed;
    }

    // ---------- helpers ----------

    public User getUser(String userId) {
        User user = users.get(userId);
        if (user == null) throw new RuntimeException("User not found: " + userId);
        return user;
    }

    public Post getPost(int postId) {
        Post post = posts.get(postId);
        if (post == null) throw new RuntimeException("Post not found: " + postId);
        return post;
    }
}
