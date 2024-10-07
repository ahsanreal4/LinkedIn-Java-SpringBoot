package com.jobs.linkedIn.services.impl;

import com.jobs.linkedIn.config.security.JwtTokenProvider;
import com.jobs.linkedIn.dto.post.CreatePostDto;
import com.jobs.linkedIn.dto.post.PostDto;
import com.jobs.linkedIn.dto.post.comment.PostCommentDto;
import com.jobs.linkedIn.entities.post.Post;
import com.jobs.linkedIn.entities.post.PostComment;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.post.PostRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.PostService;
import com.jobs.linkedIn.utils.RoleUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository, JwtTokenProvider jwtTokenProvider,
                           UserRepository userRepository) {
        this.postRepository = postRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public PostDto createPost(CreatePostDto createPostDto) {
        Post post = new Post();
        post.setTitle(createPostDto.getTitle());
        post.setDescription(createPostDto.getDescription());
        post.setPostedAt(new Date());

        User user = userRepository.findById(createPostDto.getPostedBy())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user does not exist"));

        post.setPostedBy(user);

        Post savedPost = postRepository.save(post);

        return mapToDto(savedPost);
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "post does not exist"));

        Set<PostComment> commentSet = post.getComments();

        PostDto postDto = mapToDto(post);

        postDto.setComments(commentSet.stream().map(this::mapToCommentDto).collect(Collectors.toList()));

        return postDto;
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        return posts.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public String deletePostById(long id, String token) {
        String email = jwtTokenProvider.getEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user does not exist"));

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "post does not exist"));

        boolean isAdmin = new RoleUtils().isAdmin(user.getRoles());

        if (!isAdmin && !post.getPostedBy().getId().equals(user.getId())) throw new ApiException(HttpStatus.BAD_REQUEST, "you cannot delete someone else post");

        postRepository.delete(post);

        return "Post deleted successfully";
    }

    private PostCommentDto mapToCommentDto(PostComment comment) {
        PostCommentDto commentDto = new PostCommentDto();
        commentDto.setId(comment.getId());
        commentDto.setPostedAt(comment.getPostedAt());
        commentDto.setPost(comment.getPost().getId());
        commentDto.setUser(comment.getUser().getId());
        commentDto.setParent(comment.getParent().getId());
        commentDto.setText(comment.getText());

        return commentDto;
    }

    private PostDto mapToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setPostedAt(post.getPostedAt());
        postDto.setPostedBy(post.getPostedBy().getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());

        return postDto;
    }
}
