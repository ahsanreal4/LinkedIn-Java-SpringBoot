package com.jobs.linkedIn.services;

import com.jobs.linkedIn.dto.post.CreatePostDto;
import com.jobs.linkedIn.dto.post.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(CreatePostDto createPostDto);
    PostDto getPostById(long id);
    List<PostDto> getAllPosts();
    String deletePostById(long id, String token);
}
