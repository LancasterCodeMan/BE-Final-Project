package product.catalog.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import product.catalog.controller.model.ProductCatalogData;
import product.catalog.controller.model.ProductCatalogData.ProductCatalogCategory;
import product.catalog.controller.model.ProductCatalogData.ProductCatalogReview;
import product.catalog.dao.CategoryDao;
import product.catalog.dao.ProductDao;
import product.catalog.dao.ReviewDao;
import product.catalog.entity.Category;
import product.catalog.entity.Product;
import product.catalog.entity.Review;

@Service
public class ProductCatalogService {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ReviewDao reviewDao;

	@Autowired
	private CategoryDao categoryDao;

	// Service Method to add Product
	@Transactional(readOnly = false)
	public ProductCatalogData saveProduct(ProductCatalogData productCatalogData) {
		Long productId = productCatalogData.getProductId();
		Product product = findOrCreateProduct(productId);
		copyProductFields(product, productCatalogData);
		return new ProductCatalogData(productDao.save(product));
	}

	private void copyProductFields(Product product, ProductCatalogData productCatalogData) {
		product.setProductId(productCatalogData.getProductId());
		product.setType(productCatalogData.getType());
		product.setTitle(productCatalogData.getTitle());
		product.setPrice(productCatalogData.getPrice());
	}

	private Product findOrCreateProduct(Long productId) {
		Product product;
		if (Objects.isNull(productId)) {
			product = new Product();
		} else {
			product = findProductById(productId);
		}
		return product;
	}

	private Product findProductById(Long productId) {
		return productDao.findById(productId)
				.orElseThrow(() -> new NoSuchElementException("Product with ID =" + productId + " does not exist."));
	}

	// Service Method to Add Review to Product
	@Transactional(readOnly = false)
	public ProductCatalogReview saveReview(Long productId, ProductCatalogReview productCatalogReview) {
		Product product = findProductById(productId);
		Long reviewId = productCatalogReview.getReviewId();
		Review review = findOrCreateReview(productId, reviewId);
		copyReviewFields(review, productCatalogReview);
		review.setProducts(product);
		product.getReviews().add(review);
		return new ProductCatalogReview(reviewDao.save(review));
	}

	private void copyReviewFields(Review review, ProductCatalogReview productCatalogReview) {
		review.setReviewId(productCatalogReview.getReviewId());
		review.setRating(productCatalogReview.getRating());
		review.setComment(productCatalogReview.getComment());
	}

	private Review findOrCreateReview(Long productId, Long reviewId) {
		Review review;
		if ((Objects.isNull(reviewId))) {
			review = new Review();
		} else {
			review = findReviewById(productId, reviewId);
		}
		return review;
	}

	private Review findReviewById(Long productId, Long reviewId) {
		Review review = reviewDao.findById(reviewId).orElseThrow(() -> new NoSuchElementException("Review not found"));
		if (review.getProducts().getProductId() != productId) {
			throw new IllegalArgumentException("Review does not belog to product");
		}
		return review;
	}

	// Service Methods to add Category without Product
	@Transactional(readOnly = false)
	public ProductCatalogCategory saveCategory(ProductCatalogCategory productCatalogCategory) {
		Long categoryId = productCatalogCategory.getCategoryId();
		Category category = findOrCreateCategory(categoryId);
		copyCategoryFields(category, productCatalogCategory);
		return new ProductCatalogCategory(categoryDao.save(category));
	}

	private Category findOrCreateCategory(Long categoryId) {
		Category category;
		if (Objects.isNull(categoryId)) {
			category = new Category();
		} else {
			category = findCategoryById(categoryId);
		}
		return category;
	}

	private Category findCategoryById(Long categoryId) {
		return categoryDao.findById(categoryId)
				.orElseThrow(() -> new NoSuchElementException("Category with ID= " + categoryId + " does not exist."));
	}

	// Service Methods to Add/Modify Category to Product

	@Transactional(readOnly = false)
	public ProductCatalogCategory saveProductCategory(Long productId, ProductCatalogCategory productCatalogCategory) {
		Product product = findProductById(productId);
		Long categoryId = productCatalogCategory.getCategoryId();
		Category category = findOrCreateProductCategory(productId, categoryId);
		copyCategoryFields(category, productCatalogCategory);
		category.getProducts().add(product);
		product.getCategories().add(category);
		return new ProductCatalogCategory(categoryDao.save(category));
	}

	private void copyCategoryFields(Category category, ProductCatalogCategory productCatalogCategory) {
		category.setCategoryId(productCatalogCategory.getCategoryId());
		category.setType(productCatalogCategory.getType());
	}

	private Category findOrCreateProductCategory(Long productId, Long categoryId) {
		Category category;
		if ((Objects.isNull(categoryId))) {
			category = new Category();
		} else {
			category = findProductCategoryById(productId, categoryId);
		}
		return category;
	}

	private Category findProductCategoryById(Long productId, Long categoryId) {
		Category category = categoryDao.findById(categoryId)
				.orElseThrow(() -> new NoSuchElementException("Category with ID=" + categoryId + " does not exist."));
		for (Product product : category.getProducts()) {
			if (product.getProductId() != productId) {
				throw new IllegalArgumentException("Category does not belong to product.");
			}
		}
		return category;
	}

	// Service Methods to Retrieve only list of products
	@Transactional(readOnly = true)
	public List<ProductCatalogData> retrieveAllProducts() {
		List<Product> products = productDao.findAll();
		List<ProductCatalogData> result = new LinkedList<>();

		for (Product product : products) {
			ProductCatalogData pcd = new ProductCatalogData(product);
			pcd.getCategories().clear();
			pcd.getReviews().clear();

			result.add(pcd);
		}
		return result;
	}

	// Service Method to Retrieve product by ID with Reviews and Categories
	@Transactional(readOnly = true)
	public ProductCatalogData retrieveProductById(Long productId) {
		Product product = findProductById(productId);
		return new ProductCatalogData(product);
	}

	// Service Method to Delete Product by ID Including Reviews but not Categories
	@Transactional(readOnly = false)
	public void deleteProductById(Long productId) {
		Product product = findProductById(productId);
		productDao.delete(product);
	
	}

	// Service Method to Delete Category without Product
	@Transactional(readOnly = false)
	public void deleteCategoryById(Long categoryId) {
		Category category = findCategoryById(categoryId);
		categoryDao.delete(category);		
	}

}
