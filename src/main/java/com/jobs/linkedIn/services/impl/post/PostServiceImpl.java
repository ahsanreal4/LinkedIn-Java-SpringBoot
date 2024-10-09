package com.jobs.linkedIn.services.impl.post;

import com.jobs.linkedIn.dto.post.CreatePostDto;
import com.jobs.linkedIn.dto.post.PostDto;
import com.jobs.linkedIn.dto.post.comment.PostCommentDto;
import com.jobs.linkedIn.entities.post.Post;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.post.PostRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.post.CommentService;
import com.jobs.linkedIn.services.post.PostLikeService;
import com.jobs.linkedIn.services.post.PostService;
import com.jobs.linkedIn.utils.UserUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;
    private final PostLikeService postLikeService;

    public PostServiceImpl(PostRepository postRepository,
                           UserRepository userRepository, CommentService commentService,
                           PostLikeService postLikeService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentService = commentService;
        this.postLikeService = postLikeService;
    }

    @Override
    public PostDto createPost(CreatePostDto createPostDto) {
        String email = new UserUtils().getEmail();

        Post post = new Post();
        post.setTitle(createPostDto.getTitle());
        post.setDescription(createPostDto.getDescription());
        post.setPostedAt(new Date());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user does not exist"));

        post.setPostedBy(user);

        Post savedPost = postRepository.save(post);

        return mapToDto(savedPost);
    }

    private PostDto getPostData(Post post, boolean ignoreFetchingComments) {
        PostDto postDto = mapToDto(post);

        long id = post.getId();

        if(!ignoreFetchingComments) {
            List<PostCommentDto> commentsDto = this.commentService.getPostComments(id);
            postDto.setComments(commentsDto);
            postDto.setNumComments(commentsDto.size());
        }
        else {
            int numComments = this.commentService.getNumberOfPostComments(id);
            postDto.setNumComments(numComments);
        }

        int numLikes = this.postLikeService.getNumberOfPostLikes(id);

        boolean isLiked = false;

        // Only check if likes on post are greater than 0
        if(numLikes > 0) isLiked = this.postLikeService.isLikedByUser(id);

        postDto.setNumLikes(numLikes);
        postDto.setLiked(isLiked);

        return postDto;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "post does not exist"));

        return getPostData(post, false);
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        return posts.stream().map(post -> getPostData(post, true)).collect(Collectors.toList());
    }

    @Override
    public String deletePostById(long id) {
        String email = new UserUtils().getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user does not exist"));

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "post does not exist"));

        boolean isAdmin = new UserUtils().isAdmin(user.getRoles());

        if (!isAdmin && !post.getPostedBy().getId().equals(user.getId())) throw new ApiException(HttpStatus.BAD_REQUEST, "you cannot delete someone else post");

        postRepository.delete(post);

        return "Post deleted successfully";
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
