package com.jobs.linkedIn.dto.post.likes;

import lombok.Data;

@Data
public class PostLikeDto {
    private Long id;
    private Long userId;
    private Long postId;
}
