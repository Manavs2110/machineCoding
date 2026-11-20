package org.instagramFollowFeature.model;

import java.util.HashSet;
import java.util.Set;

public class User {
    String userId;
    String name;
    Set<String> followers = new HashSet<>();   // ids of users who follow me
    Set<String> following = new HashSet<>();   // ids of users I follow

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Set<String> getFollowers() {
        return followers;
    }

    public Set<String> getFollowing() {
        return following;
    }
}
