package org.videoSharingPlatform.model;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Video {
    String videoId;
    String uploaderId;
    String title;
    String description;
    String videoUrl;
    Instant uploadedAt;
    AtomicLong viewCount = new AtomicLong(0);   // atomic so concurrent watches are never lost

    public Video(String uploaderId, String title, String description, String videoUrl) {
        this.videoId = UUID.randomUUID().toString();
        this.uploaderId = uploaderId;
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
        this.uploadedAt = Instant.now();
    }

    public long addView(){
        return viewCount.incrementAndGet();
    }

    public String getVideoId() {
        return videoId;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public long getViewCount() {
        return viewCount.get();
    }

    @Override
    public String toString() {
        return title + " by " + uploaderId + " (" + getViewCount() + " views)";
    }
}
