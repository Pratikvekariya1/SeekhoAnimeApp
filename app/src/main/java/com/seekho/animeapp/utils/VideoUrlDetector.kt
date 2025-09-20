package com.seekho.animeapp.utils

import java.net.URL

object VideoUrlDetector {
    
    /**
     * Detect the type of video URL and return appropriate player type
     */
    sealed class VideoUrlType {
        data class YouTube(val videoId: String) : VideoUrlType()
        data class Direct(val url: String) : VideoUrlType()
        object Invalid : VideoUrlType()
    }
    
    /**
     * Analyze URL and determine the appropriate player type
     */
    fun detectVideoUrlType(url: String): VideoUrlType {
        return when {
            isYouTubeUrl(url) -> {
                val videoId = extractYouTubeVideoId(url)
                if (videoId != null) {
                    VideoUrlType.YouTube(videoId)
                } else {
                    VideoUrlType.Invalid
                }
            }
            isDirectVideoUrl(url) -> VideoUrlType.Direct(url)
            else -> VideoUrlType.Invalid
        }
    }
    
    /**
     * Check if URL is from YouTube
     */
    private fun isYouTubeUrl(url: String): Boolean {
        return url.contains("youtube.com", ignoreCase = true) ||
                url.contains("youtu.be", ignoreCase = true) ||
                url.contains("youtube-nocookie.com", ignoreCase = true)
    }
    
    /**
     * Check if URL is a direct video URL (supports common video formats)
     */
    private fun isDirectVideoUrl(url: String): Boolean {
        val videoExtensions = listOf(
            "mp4", "mkv", "avi", "mov", "wmv", "flv", "webm", 
            "m4v", "3gp", "ts", "m3u8", "mpd"
        )
        
        return try {
            val urlObj = URL(url.lowercase())
            val path = urlObj.path.lowercase()
            
            // Check for direct file extensions
            videoExtensions.any { extension ->
                path.endsWith(".$extension") || path.contains(".$extension?")
            } ||
            // Check for streaming protocols
            url.startsWith("rtmp://", ignoreCase = true) ||
            url.startsWith("rtsp://", ignoreCase = true) ||
            // Check for adaptive streaming manifests
            path.contains(".m3u8") ||
            path.contains(".mpd") ||
            // Check for common video hosting patterns
            url.contains("videoplayback", ignoreCase = true) ||
            url.contains("/video/", ignoreCase = true)
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Extract YouTube video ID from various YouTube URL formats
     */
    private fun extractYouTubeVideoId(url: String): String? {
        // Use non-lookbehind patterns that work on Android
        val patterns = listOf(
            // youtube.com/watch?v=VIDEO_ID
            "youtube\\.com/watch\\?v=([^#&?]*)".toRegex(),

            // youtu.be/VIDEO_ID
            "youtu\\.be/([^#&?]*)".toRegex(),

            // youtube.com/embed/VIDEO_ID
            "youtube\\.com/embed/([^#&?]*)".toRegex(),

            // youtube.com/v/VIDEO_ID
            "youtube\\.com/v/([^#&?]*)".toRegex(),

            // youtube-nocookie.com/embed/VIDEO_ID
            "youtube-nocookie\\.com/embed/([^#&?]*)".toRegex(),

            // Handle additional YouTube URL patterns
            "youtube\\.com/watch.*[?&]v=([^#&?]*)".toRegex(),

            // Mobile YouTube URLs
            "m\\.youtube\\.com/watch\\?v=([^#&?]*)".toRegex()
        )

        for (pattern in patterns) {
            val match = pattern.find(url)
            if (match != null && match.groupValues.size > 1) {
                val videoId = match.groupValues[1]
                if (videoId.isNotEmpty() && isValidYouTubeVideoId(videoId)) {
                    return videoId
                }
            }
        }

        return null
    }

    /**
     * Validate if the extracted string is a valid YouTube video ID
     */
    private fun isValidYouTubeVideoId(videoId: String): Boolean {
        // YouTube video IDs are typically 11 characters long
        // and contain alphanumeric characters, underscores, and hyphens
        return videoId.length == 11 && videoId.matches("[a-zA-Z0-9_-]+".toRegex())
    }
    
    /**
     * Get appropriate MIME type for direct video URLs
     */
    fun getMimeType(url: String): String {
        return when {
            url.contains(".m3u8", ignoreCase = true) -> "application/x-mpegURL"
            url.contains(".mpd", ignoreCase = true) -> "application/dash+xml"
            url.contains(".mp4", ignoreCase = true) -> "video/mp4"
            url.contains(".mkv", ignoreCase = true) -> "video/x-matroska"
            url.contains(".webm", ignoreCase = true) -> "video/webm"
            url.contains(".avi", ignoreCase = true) -> "video/x-msvideo"
            url.contains(".mov", ignoreCase = true) -> "video/quicktime"
            url.contains(".wmv", ignoreCase = true) -> "video/x-ms-wmv"
            url.contains(".flv", ignoreCase = true) -> "video/x-flv"
            url.contains(".3gp", ignoreCase = true) -> "video/3gpp"
            url.contains(".ts", ignoreCase = true) -> "video/mp2ts"
            else -> "video/*"
        }
    }
}
