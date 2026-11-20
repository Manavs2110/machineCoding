package org.videoSharingPlatform.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.videoSharingPlatform.model.Video;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoService {
    Map<String, Video> videosById = new HashMap<>();
    Map<String, Video> videosByUploaderAndTitle = new HashMap<>();   // key: uploaderId + "|" + title

    private String uploaderAndTitleKey(String uploaderId, String title) {
        return uploaderId + "|" + title;
    }

    public boolean isValidUrl(String url) {
        if (url == null || url.isBlank()) return false;
        try {
            URL parsedUrl = new URL(url);
            String protocol = parsedUrl.getProtocol();
            boolean isHttp = protocol.equals("http") || protocol.equals("https");
            return isHttp && !parsedUrl.getHost().isEmpty();
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public Video saveVideo(String uploaderId, String title, String description, String url) {
        if (uploaderId == null || uploaderId.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploader id cannot be empty");
        if (title == null || title.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be empty");
        if (!isValidUrl(url)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid URL: " + url);

        String key = uploaderAndTitleKey(uploaderId, title);
        if (videosByUploaderAndTitle.containsKey(key)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Video already exists: " + title);

        Video video = new Video(uploaderId, title, description, url);
        videosById.put(video.getVideoId(), video);
        videosByUploaderAndTitle.put(key, video);
        return video;
    }

    public Video getVideo(String uploaderId, String title) {
        Video video = videosByUploaderAndTitle.get(uploaderAndTitleKey(uploaderId, title));
        if (video == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found: " + title);
        return video;
    }

    public Video watchVideo(String uploaderId, String title) {
        Video video = getVideo(uploaderId, title);
        video.addView();
        return video;
    }

    public Video getVideoById(String videoId) {
        Video video = videosById.get(videoId);
        if (video == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found: " + videoId);
        return video;
    }

    public Video watchVideoById(String videoId) {
        Video video = getVideoById(videoId);
        video.addView();
        return video;
    }

    public List<Video> getTopVideos(int limit) {
        if (limit <= 0 || limit > 100) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit must be between 1 and 100");

        List<Video> sortedVideos = new ArrayList<>(videosById.values());
        sortedVideos.sort((first, second) -> Long.compare(second.getViewCount(), first.getViewCount()));   // most views first

        if (sortedVideos.size() > limit) return sortedVideos.subList(0, limit);
        return sortedVideos;
    }
}
