package az.edu.ada.wm2.lab6.service;

import az.edu.ada.wm2.lab6.model.Category;
import az.edu.ada.wm2.lab6.model.Product;
import az.edu.ada.wm2.lab6.model.dto.CategoryRequestDto;
import az.edu.ada.wm2.lab6.model.dto.CategoryResponseDto;
import az.edu.ada.wm2.lab6.model.dto.ProductResponseDto;
import az.edu.ada.wm2.lab6.model.mapper.ProductMapper;
import az.edu.ada.wm2.lab6.repository.CategoryRepository;
import az.edu.ada.wm2.lab6.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	public CategoryServiceImpl(CategoryRepository categoryRepository,
			ProductRepository productRepository,
			ProductMapper productMapper) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
		this.productMapper = productMapper;
	}

	@Override
	public CategoryResponseDto create(CategoryRequestDto dto) {
		Category category = new Category();
		category.setName(dto.getName());

		Category saved = categoryRepository.save(category);
		return new CategoryResponseDto(saved.getId(), saved.getName());
	}

	@Override
	public List<CategoryResponseDto> getAll() {
		return categoryRepository.findAll().stream()
				.map(c -> new CategoryResponseDto(c.getId(), c.getName()))
				.collect(Collectors.toList());
	}

	@Override
	public void addProduct(UUID categoryId, UUID productId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Category not found"));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));

		if (product.getCategories() == null) {
			product.setCategories(new ArrayList<>());
		}
		product.getCategories().add(category);

		productRepository.save(product);
	}

	@Override
	public List<ProductResponseDto> getProducts(UUID categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Category not found"));

		return category.getProducts().stream()
				.map(productMapper::toResponseDto)
				.collect(Collectors.toList());
	}
}

