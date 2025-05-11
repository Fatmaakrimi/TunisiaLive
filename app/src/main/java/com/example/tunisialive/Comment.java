package com.example.tunisialive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Comment {
    private String username;
    private String userEmail;
    private String comment;
    private String profileImageUrl;  // URL of the profile picture
    private long timestamp;         // Timestamp in milliseconds
    private String commentId;       // Unique identifier for the comment

    // Required empty constructor for Firestore
    public Comment() {
        this.timestamp = System.currentTimeMillis();
    }

    public Comment(@NonNull String username,
                   @NonNull String userEmail,
                   @NonNull String comment,
                   @Nullable String profileImageUrl) {
        this.username = username;
        this.userEmail = userEmail;
        this.comment = comment;
        this.profileImageUrl = profileImageUrl;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters with null checks where appropriate
    @NonNull
    public String getUsername() {
        return username != null ? username : "Anonymous";
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    @NonNull
    public String getUserEmail() {
        return userEmail != null ? userEmail : "";
    }

    public void setUserEmail(@Nullable String userEmail) {
        this.userEmail = userEmail;
    }

    @NonNull
    public String getComment() {
        return comment != null ? comment : "";
    }

    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    @Nullable
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(@Nullable String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Nullable
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(@Nullable String commentId) {
        this.commentId = commentId;
    }

    /**
     * Formats the timestamp into a readable date string
     * @return Formatted date string (e.g. "15 Jun, 10:30 AM")
     */
    public String getFormattedTimestamp() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault());
            return sdf.format(new Date(timestamp));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Formats the timestamp into a relative time string
     * @return Relative time string (e.g. "2 hours ago")
     */
    public String getRelativeTime() {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            return days + (days == 1 ? " day ago" : " days ago");
        } else if (hours > 0) {
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        } else if (minutes > 0) {
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        } else {
            return "Just now";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return commentId != null && commentId.equals(comment.commentId);
    }

    @Override
    public int hashCode() {
        return commentId != null ? commentId.hashCode() : 0;
    }
}