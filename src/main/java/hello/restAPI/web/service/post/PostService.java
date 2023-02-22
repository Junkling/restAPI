package hello.restAPI.web.service.post;

import hello.restAPI.customException.PostUserException;
import hello.restAPI.domain.post.DeletedPost;
import hello.restAPI.domain.post.Post;
import hello.restAPI.web.dto.PostUpdateDto;
import hello.restAPI.domain.user.User;
import hello.restAPI.web.repository.post.DeletePostRepository;
import hello.restAPI.web.repository.post.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final DeletePostRepository deletePostRepository;


    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }
    public Post save(Post post, User user) {
        post.setHeartCount(0);
        post.setUser(user);
        return postRepository.save(post);
    }

    public void update(Long postId, PostUpdateDto postUpdateDto, User user) throws EntityNotFoundException, PostUserException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시물이 존재하지 않습니다."));
        if (checkWriter(user, post) == false) {
            throw new PostUserException("작성자가 아닙니다.");
        }
        post.setTitle(postUpdateDto.getTitle());
        post.setContents(postUpdateDto.getContents());
    }

    public boolean checkWriter(User user, Post post) {
        if(!post.getUser().equals(user)){
            return false;
        }
        return true;
    }

    public void delete(Long postId, User user) throws EntityNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시물이 존재하지 않습니다."));
        checkWriter(user,post);
        saveDeletedPost(post);
        postRepository.delete(post);
    }

    private void saveDeletedPost(Post post) {
        DeletedPost deletePost = new DeletedPost();
        deletePost.setUser(post.getUser());
        deletePost.setTitle(post.getTitle());
        deletePost.setContents(post.getContents());
        deletePostRepository.save(deletePost);
    }

    public List<Post> findPost() {
        return postRepository.findAll();
    }
}
