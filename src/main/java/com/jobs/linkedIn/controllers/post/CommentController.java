package com.jobs.linkedIn.controllers.post;

import com.jobs.linkedIn.dto.post.comment.CreatePostCommentDto;
import com.jobs.linkedIn.dto.post.comment.PostCommentDto;
import com.jobs.linkedIn.services.CommentService;
import com.jobs.linkedIn.utils.HeaderUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@SecurityRequirement(
        name = "Bear Authentication"
)
@Tag(
        name = "Comment",
        description = "endpoints to get, update, delete and modify post comments"
)
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("")
    public ResponseEntity<PostCommentDto> addCommentToPost(@Valid @RequestBody CreatePostCommentDto createPostCommentDto) {
        PostCommentDto postCommentDto = this.commentService.addPostComment(createPostCommentDto);

        return new ResponseEntity<>(postCommentDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostCommentDto> getCommentById(@RequestParam("id") long id) {
        PostCommentDto postCommentDto = this.commentService.getCommentById(id);

        return ResponseEntity.ok(postCommentDto);
    }

    @GetMapping("/replies/{id}")
    public ResponseEntity<List<PostCommentDto>> getReplyComments(@RequestParam("id") long id) {
        List<PostCommentDto> postCommentDtos = this.commentService.getReplyComments(id);

        return ResponseEntity.ok(postCommentDtos);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<List<PostCommentDto>> getPostComments(@RequestParam("id") long id) {
        List<PostCommentDto> postCommentDtos = this.commentService.getPostComments(id);

        return ResponseEntity.ok(postCommentDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommentById(@RequestParam("id") long id,
                                                    HttpServletRequest request) {
        String token = new HeaderUtils().getTokenFromHeader(request);

        String response = this.commentService.deleteCommentById(id, token);

        return ResponseEntity.ok(response);
    }
}
