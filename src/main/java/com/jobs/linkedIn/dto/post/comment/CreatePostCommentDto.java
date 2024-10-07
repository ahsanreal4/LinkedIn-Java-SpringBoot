package com.jobs.linkedIn.dto.post.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePostCommentDto {
    @Positive
    @NotNull
    private Long postId;

    @NotNull
    @Positive
    private Long userId;

    @Positive
    private Long parentId;

    @NotEmpty
    @Size(min = 1, max = 255)
    private String text;
}
