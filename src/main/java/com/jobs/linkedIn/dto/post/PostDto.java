package com.jobs.linkedIn.dto.post;

import com.jobs.linkedIn.dto.post.comment.PostCommentDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostDto {
    private Long id;
    private Long postedBy;
    private String title;
    private String description;
    private Date postedAt;
    private Integer numComments;
    private Integer numLikes;
    private boolean isLiked;
    private List<PostCommentDto> comments;
}
