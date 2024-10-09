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
import java.util.Set;

@Service
class CommentServiceImpl implements CommentService {

    private final PostCommentsRepository postCommentsRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(PostCommentsRepository postCommentsRepository,
                              UserRepository userRepository,
                              PostRepository postRepository) {
        this.postCommentsRepository = postCommentsRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public PostCommentDto addPostComment(CreatePostCommentDto createPostCommentDto) {
        String email = new UserUtils().getEmail();

        Post post = postRepository.findById(createPostCommentDto.getPostId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "post does not exist"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user does not exist"));

        PostComment postComment = new PostComment();
        postComment.setPostedAt(new Date());
        postComment.setPost(post);
        postComment.setUser(user);
        postComment.setText(createPostCommentDto.getText());

        Long parentId = createPostCommentDto.getParentId();

        if (parentId != null) {
            PostComment parentComment = postCommentsRepository.findById(parentId)
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "parent comment does not exist"));

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
        PostComment postComment = postCommentsRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "comment does not exist"));

        return mapToDto(postComment);
    }

    @Override
    public String deleteCommentById(long id) {
        String email = new UserUtils().getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user does not exist"));

        PostComment postComment = postCommentsRepository.findById(id)
                .orElseThrow(() -> new ApiException((HttpStatus.NOT_FOUND), "post comment does not exist"));

        boolean isAdmin = new UserUtils().isAdmin(user.getRoles());

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
