package hello.restAPI.web.service.heart;

import hello.restAPI.customException.HeartException;
import hello.restAPI.domain.heart.Heart;
import hello.restAPI.domain.post.Post;
import hello.restAPI.domain.user.User;
import hello.restAPI.web.repository.heart.HeartRepository;
import hello.restAPI.web.repository.post.PostRepository;
import hello.restAPI.web.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class HeartService {
    private final PostRepository postRepository;
    private final HeartRepository heartRepository;

    private final UserRepository userRepository;

    public void save(Heart heart, Long postId, User user) throws HeartException, IOException {

        if (heartExist(user.getId()) == true) {
            throw new IOException("좋아요한 게시물이 있습니다.");
        }

        if (heartCheck(postId,user)==true) {
            throw new HeartException("이미 좋아요 표기된 게시물");
        }
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물"));
        heart.setUser(user);
        heart.setPost(post);
        heartRepository.save(heart);
        post.setHeartCount(post.getHeartCount() + 1);
    }

    public void delete(Long postId, User user) throws HeartException, EntityNotFoundException{
        Heart heart = heartRepository.findByUserAndPostId(user, postId).orElse(null);
        if (heartCheck(postId,user)==false) {
            throw new HeartException("좋아요된 게시물이 없습니다.");
        }
        heartRepository.delete(heart);

        Post findPost = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물"));
        findPost.setHeartCount(findPost.getHeartCount() - 1);
    }

    public Boolean heartCheck(Long postId, User user) {
        Heart heart = heartRepository.findByUserAndPostId(user, postId).orElse(null);
        if (heart == null) {
            return false;
        }
        return true;
    }

    public Boolean heartExist(Long userId) {
        if (heartRepository.findByUserId(userId).isPresent() == true) {
            return true;
        }
        return false;
    }
}
