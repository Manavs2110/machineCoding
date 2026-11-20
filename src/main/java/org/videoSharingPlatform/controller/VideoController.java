package org.videoSharingPlatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.videoSharingPlatform.model.Video;
import org.videoSharingPlatform.service.VideoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    @Autowired
    VideoService videoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Video saveVideo(@RequestBody Map<String, String> body) {
        return videoService.saveVideo(body.get("uploaderId"), body.get("title"), body.get("description"), body.get("videoUrl"));
    }

    @GetMapping
    public Video fetchVideo(@RequestParam String uploaderId, @RequestParam String title) {
        return videoService.getVideo(uploaderId, title);
    }

    // body: { "uploaderId", "title" }
    @PostMapping("/watch")
    public Video watchVideo(@RequestBody Map<String, String> body) {
        return videoService.watchVideo(body.get("uploaderId"), body.get("title"));
    }

    @GetMapping("/{videoId}")
    public Video fetchVideoById(@PathVariable String videoId) {
        return videoService.getVideoById(videoId);
    }

    @PostMapping("/{videoId}/views")
    public Video watchVideoById(@PathVariable String videoId) {
        return videoService.watchVideoById(videoId);
    }

    @GetMapping("/top")
    public List<Video> topVideos(@RequestParam(defaultValue = "10") int limit) {
        return videoService.getTopVideos(limit);
    }
}
