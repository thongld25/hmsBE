package thongld25.hms.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thongld25.hms.dtos.requests.PostRequest;
import thongld25.hms.dtos.responses.ApiResponse;
import thongld25.hms.dtos.responses.PostResponse;
import thongld25.hms.service.PostService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/posts")
public class PostController {
    PostService postService;

    @GetMapping
    public ApiResponse<List<PostResponse>> getAllPost(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.getAllPost(pageNo, pageSize))
                .build();
    }

    @GetMapping("/doctor")
    public ApiResponse<List<PostResponse>> getPostOfDoctor(
            @RequestParam String doctorId,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.getPostOfDoctor(doctorId, pageNo, pageSize))
                .build();
    }

    @GetMapping("/category")
    public ApiResponse<List<PostResponse>> getPostOfCategory(
            @RequestParam UUID categoryId,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.getPostOfCategory(categoryId ,pageNo, pageSize))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PostResponse> getPostById(@PathVariable UUID id) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.getPostById(id))
                .build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<PostResponse> createPost(@ModelAttribute @Valid PostRequest request)
            throws IOException {
        return ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request))
                .build();
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<PostResponse> updatePost(
            @PathVariable UUID postId,
            @ModelAttribute PostRequest request) throws IOException {
        return ApiResponse.<PostResponse>builder()
                .result(postService.updatePost(request, postId))
                .build();
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<HttpStatus> deletePost(@PathVariable UUID postId) {
        postService.deletePost(postId);
        return ApiResponse.<HttpStatus>builder()
                .result(HttpStatus.NO_CONTENT)
                .build();
    }
}
