package com.jobs.linkedIn.controllers.post;

import com.jobs.linkedIn.dto.post.likes.CreateLikeDto;
import com.jobs.linkedIn.services.post.PostLikeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/likes")
@SecurityRequirement(
        name = "Bear Authentication"
)
@Tag(
        name = "Post",
        description = "endpoints to get, update, delete and modify post likes"
)
public class PostLikeController {

    private final PostLikeService postLikeService;

    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }


    @PostMapping("")
    public ResponseEntity<String> likePost(@Valid @RequestBody CreateLikeDto createLikeDto) {
        String response = this.postLikeService.likePost(createLikeDto.getPostId());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
