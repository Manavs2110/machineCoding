package org.instagramFollowFeature;

import org.instagramFollowFeature.model.Post;
import org.instagramFollowFeature.service.InstagramService;

public class Main {
    public static void main(String args[]){
        InstagramService instagram = new InstagramService();
        instagram.createUser("alice", "Alice");
        instagram.createUser("bob", "Bob");
        instagram.createUser("carol", "Carol");

        // follow
        instagram.follow("alice", "bob");
        instagram.follow("alice", "carol");
        instagram.follow("bob", "carol");
        System.out.println("carol's followers: " + instagram.getUser("carol").getFollowers());   // alice and bob (a set, so any order)

        // post
        Post bobPost = instagram.createPost("bob", "Morning run");
        Post carolPost = instagram.createPost("carol", "New painting");

        // like (twice by alice still counts once)
        instagram.likePost("alice", bobPost.getPostId());
        instagram.likePost("alice", bobPost.getPostId());
        instagram.likePost("carol", bobPost.getPostId());

        // comment
        instagram.commentOnPost("alice", carolPost.getPostId(), "Love the colours!");
        instagram.commentOnPost("bob", carolPost.getPostId(), "Amazing");

        System.out.println(bobPost);                                  // 2 likes
        System.out.println(carolPost + " " + carolPost.getComments());

        // feed: newest first, only people alice follows
        System.out.println("alice's feed: " + instagram.getFeed("alice"));   // carol's post, then bob's

        instagram.unfollow("alice", "carol");
        System.out.println("alice's feed after unfollow: " + instagram.getFeed("alice"));   // only bob's

        // errors
        try {
            instagram.follow("alice", "alice");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
        try {
            instagram.likePost("alice", 99);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
