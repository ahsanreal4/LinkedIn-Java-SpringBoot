package com.jobs.linkedIn.controllers.post;

import com.jobs.linkedIn.dto.post.CreatePostDto;
import com.jobs.linkedIn.dto.post.PostDto;
import com.jobs.linkedIn.services.post.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@SecurityRequirement(
        name = "Bear Authentication"
)
@Tag(
        name = "Post",
        description = "endpoints to get, update, delete and modify post"
)
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody CreatePostDto createPostDto) {
        PostDto createdPost = this.postService.createPost(createPostDto);

        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<PostDto>> getPostById() {
        List<PostDto> posts = this.postService.getAllPosts();

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") long id) {
        PostDto post = this.postService.getPostById(id);

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable("id") long id) {
        String response = this.postService.deletePostById(id);

        return ResponseEntity.ok(response);
    }
}
