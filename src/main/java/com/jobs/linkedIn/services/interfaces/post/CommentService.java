package com.jobs.linkedIn.services.interfaces.post;

import com.jobs.linkedIn.dto.post.comment.CreatePostCommentDto;
import com.jobs.linkedIn.dto.post.comment.PostCommentDto;

import java.util.List;

public interface CommentService {
    PostCommentDto addPostComment(CreatePostCommentDto createPostCommentDto);
    List<PostCommentDto> getPostComments(long id);
    int getNumberOfPostComments(long id);
    List<PostCommentDto> getReplyComments(long id);
    PostCommentDto getCommentById(long id);
    String deleteCommentById(long id);
}
