package thongld25.hms.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import thongld25.hms.dtos.requests.CategoryRequest;
import thongld25.hms.dtos.responses.CategoryResponse;
import thongld25.hms.entity.Category;
import thongld25.hms.exceptions.AppException;
import thongld25.hms.exceptions.ErrorCode;
import thongld25.hms.repository.CategoryRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    ModelMapper modelMapper;

    public List<CategoryResponse> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            throw new AppException(ErrorCode.NO_CATEGORY_FOUND);
        }
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .collect(Collectors.toList());
    }

    public CategoryResponse addCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        categoryRepository.save(category);
        return modelMapper.map(category, CategoryResponse.class);
    }

    public void deleteCategory (UUID id) {
        categoryRepository.deleteById(id);
    }
}
