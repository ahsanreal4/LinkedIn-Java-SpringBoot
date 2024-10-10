package com.jobs.linkedIn.services.impl.post;

import com.jobs.linkedIn.dto.post.comment.CreatePostCommentDto;
import com.jobs.linkedIn.dto.post.comment.PostCommentDto;
import com.jobs.linkedIn.entities.post.Post;
import com.jobs.linkedIn.entities.post.PostComment;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.post.PostCommentsRepository;
import com.jobs.linkedIn.repositories.post.PostRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.post.CommentService;
import com.jobs.linkedIn.utils.UserUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {

    private final PostCommentsRepository postCommentsRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserUtils userUtils;

    public CommentServiceImpl(PostCommentsRepository postCommentsRepository,
                              UserRepository userRepository,
                              PostRepository postRepository, UserUtils userUtils) {
        this.postCommentsRepository = postCommentsRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.userUtils = userUtils;
    }

    @Override
    public PostCommentDto addPostComment(CreatePostCommentDto createPostCommentDto) {
        String email = userUtils.getEmail();

        Optional<Post> optionalPost = postRepository.findById(createPostCommentDto.getPostId());

        if (optionalPost.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "post does not exist");

        Post post = optionalPost.get();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "user does not exist");

        User user = optionalUser.get();

        PostComment postComment = new PostComment();
        postComment.setPostedAt(new Date());
        postComment.setPost(post);
        postComment.setUser(user);
        postComment.setText(createPostCommentDto.getText());

        Long parentId = createPostCommentDto.getParentId();

        if (parentId != null) {
            Optional<PostComment> optionalPostComment = postCommentsRepository.findById(parentId);

            if (optionalPostComment.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "parent comment does not exist");

            PostComment parentComment = optionalPostComment.get();

            postComment.setParent(parentComment);
        };

        PostComment savedComment = postCommentsRepository.save(postComment);

        return mapToDto(savedComment);
    }

    @Override
    public List<PostCommentDto> getPostComments(long id) {
        Set<PostComment> comments = postCommentsRepository.findByPostIdAndParentId(id, null);

        return comments.stream().map(this::mapToDto).toList();
    }

    @Override
    public int getNumberOfPostComments(long id) {
        return postCommentsRepository.countByPostId(id);
    }

    @Override
    public List<PostCommentDto> getReplyComments(long id) {
        Set<PostComment> comments = postCommentsRepository.findByParentId(id);

        return comments.stream().map(this::mapToDto).toList();
    }

    @Override
    public PostCommentDto getCommentById(long id) {
        Optional<PostComment> optionalPostComment = postCommentsRepository.findById(id);

        if (optionalPostComment.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "comment does not exist");

        PostComment postComment = optionalPostComment.get();

        return mapToDto(postComment);
    }

    @Override
    public String deleteCommentById(long id) {
        String email = userUtils.getEmail();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "user does not exist");

        User user = optionalUser.get();

        Optional<PostComment> optionalPostComment = postCommentsRepository.findById(id);

        if(optionalPostComment.isEmpty()) throw new ApiException((HttpStatus.NOT_FOUND), "post comment does not exist");

        PostComment postComment = optionalPostComment.get();

        boolean isAdmin = userUtils.isAdmin(user.getRoles());

        if (!isAdmin && !postComment.getUser().getId().equals(user.getId())) throw new ApiException(HttpStatus.BAD_REQUEST, "you cannot delete someone else comment");

        postCommentsRepository.delete(postComment);

        return "Post Comment deleted successfully";
    }

    private PostCommentDto mapToDto(PostComment comment) {
        PostCommentDto dto = new PostCommentDto();
        dto.setId(comment.getId());
        dto.setUser(comment.getUser().getId());
        dto.setText(comment.getText());
        if(comment.getParent() != null) dto.setParent(comment.getParent().getId());
        dto.setPost(comment.getPost().getId());
        dto.setPostedAt(comment.getPostedAt());

        return dto;
    }

}
