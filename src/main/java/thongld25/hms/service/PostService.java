package thongld25.hms.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import thongld25.hms.dtos.requests.DefaultImage;
import thongld25.hms.dtos.requests.PostRequest;
import thongld25.hms.dtos.responses.PostResponse;
import thongld25.hms.entity.Doctor;
import thongld25.hms.entity.Post;
import thongld25.hms.exceptions.AppException;
import thongld25.hms.exceptions.ErrorCode;
import thongld25.hms.repository.CategoryRepository;
import thongld25.hms.repository.DoctorRepository;
import thongld25.hms.repository.PostRepository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    PostRepository postRepository;
    ModelMapper modelMapper;
    UploadFile uploadFile;
    DoctorRepository doctorRepository;
    CategoryRepository categoryRepository;

    public List<PostResponse> getAllPost(int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
        List<Post> posts = postRepository.findAll(pageRequest).stream().toList();
        if(posts.isEmpty()) {
            throw new AppException(ErrorCode.NO_POST_FOUND);
        }

        return posts.stream()
                .map(post -> modelMapper.map(post, PostResponse.class))
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostOfDoctor(String doctorId, int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
        List<Post> posts = postRepository.findByDoctorId(doctorId, pageRequest);
        if(posts.isEmpty()) {
            throw new AppException(ErrorCode.NO_POST_FOUND);
        }

        return posts.stream()
                .map(post -> modelMapper.map(post, PostResponse.class))
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostOfCategory(UUID categoryId, int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
        List<Post> posts = postRepository.findByCategoryId(categoryId, pageRequest);
        if(posts.isEmpty()) {
            throw new AppException(ErrorCode.NO_POST_FOUND);
        }

        return posts.stream()
                .map(post -> modelMapper.map(post, PostResponse.class))
                .collect(Collectors.toList());
    }

    public PostResponse getPostById(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new AppException(ErrorCode.NO_POST_FOUND));
        return modelMapper.map(post, PostResponse.class);
    }

    public PostResponse createPost(PostRequest postRequest) throws IOException {
        String url;
        if(!postRequest.getCover().isEmpty()) {
            url = uploadFile.uploadImage(postRequest.getCover());
        }
        else url = DefaultImage.POST;
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setSummary(postRequest.getSummary());
        post.setCoverContent(postRequest.getCoverContent());
        post.setCover(url);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        post.setDoctor(doctorRepository.findByPhoneNumber(username));
        post.setCategory(categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(()-> new AppException(ErrorCode.NO_CATEGORY_FOUND)));
        postRepository.save(post);
        return modelMapper.map(post, PostResponse.class);
    }

    public PostResponse updatePost(PostRequest postRequest, UUID postId) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.NO_POST_FOUND));
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setSummary(postRequest.getSummary());
        post.setCoverContent(postRequest.getCoverContent());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Doctor doctor = doctorRepository.findByPhoneNumber(username);
        if (doctor == null || doctor.getId().compareTo(post.getDoctor().getId()) != 0) {
            throw new RuntimeException("error");
        }

        if (postRequest.getCover() != null) {
            String url = uploadFile.uploadImage(postRequest.getCover());
            post.setCover(url);
        }

        if (postRequest.getCategoryId() != null) {
            post.setCategory(categoryRepository.findById(postRequest.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.NO_CATEGORY_FOUND)));
        }

        postRepository.save(post);
        return modelMapper.map(post, PostResponse.class);
    }

    public void deletePost(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new AppException(ErrorCode.NO_POST_FOUND));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Doctor doctor = doctorRepository.findByPhoneNumber(username);
        if(doctor.getId().compareTo(post.getDoctor().getId()) != 0) {
            throw new RuntimeException("error");
        }
        postRepository.deleteById(postId);
    }
}
