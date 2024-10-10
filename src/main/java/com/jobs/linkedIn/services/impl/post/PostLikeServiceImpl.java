package com.jobs.linkedIn.services.impl.post;

import com.jobs.linkedIn.dto.post.likes.PostLikeDto;
import com.jobs.linkedIn.entities.post.Post;
import com.jobs.linkedIn.entities.post.PostLikes;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.post.PostLikesRepository;
import com.jobs.linkedIn.repositories.post.PostRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.post.PostLikeService;
import com.jobs.linkedIn.utils.UserUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikesRepository postLikesRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserUtils userUtils;

    public PostLikeServiceImpl(PostLikesRepository postLikesRepository,
                               UserRepository userRepository,
                               PostRepository postRepository, UserUtils userUtils) {
        this.postLikesRepository = postLikesRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.userUtils = userUtils;
    }

    @Override
    public String likePost(long postId) {
        String email = userUtils.getEmail();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "user does not exist");

        User user = optionalUser.get();

        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "post does not exist");

        Post post = optionalPost.get();

        PostLikes prevPostLikes = postLikesRepository.findByUserIdAndPostId(user.getId(), postId);

        if (prevPostLikes != null) {
            postLikesRepository.delete(prevPostLikes);
            return "Post Like removed";
        }

        PostLikes postLikes = new PostLikes();
        postLikes.setPost(post);
        postLikes.setUser(user);
        postLikesRepository.save(postLikes);
        return "Post Liked";
    }

    @Override
    public List<PostLikeDto> getPostLikes(long postId) {
        List<PostLikes> postLikes = postLikesRepository.findByPostId(postId);

        return postLikes.stream().map(this::mapToDto).toList();
    }

    @Override
    public int getNumberOfPostLikes(long postId) {
        return postLikesRepository.countByPostId(postId);
    }

    @Override
    public boolean isLikedByUser(long postId) {
        String email = userUtils.getEmail();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "user does not exist");

        User user = optionalUser.get();

        return postLikesRepository.existsByUserIdAndPostId(user.getId(), postId);
    }

    private PostLikeDto mapToDto(PostLikes postLike) {
        PostLikeDto postLikeDto = new PostLikeDto();
        postLikeDto.setId(postLike.getId());
        postLikeDto.setPostId(postLike.getPost().getId());
        postLikeDto.setUserId(postLike.getUser().getId());

        return postLikeDto;
    }
}
