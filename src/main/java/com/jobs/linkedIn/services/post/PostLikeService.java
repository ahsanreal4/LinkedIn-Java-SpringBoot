package com.jobs.linkedIn.services.post;

import com.jobs.linkedIn.dto.post.likes.PostLikeDto;

import java.util.List;

public interface PostLikeService {
    String likePost(long postId);
    List<PostLikeDto> getPostLikes(long postId);
    int getNumberOfPostLikes(long postId);
    boolean isLikedByUser(long postId);
}
