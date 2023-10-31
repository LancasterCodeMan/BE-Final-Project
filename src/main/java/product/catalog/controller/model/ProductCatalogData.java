package product.catalog.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import product.catalog.entity.Category;
import product.catalog.entity.Product;
import product.catalog.entity.Review;

@Data
@NoArgsConstructor
public class ProductCatalogData {
	private Long productId;
	private String type;
	private String title;
	private Double price;
	private Set<ProductCatalogReview> reviews = new HashSet<>();
	private Set<ProductCatalogCategory> categories = new HashSet<>();

	public ProductCatalogData(Product product) {
		productId = product.getProductId();
		type = product.getType();
		title = product.getTitle();
		price = product.getPrice();

		for (Review review : product.getReviews()) {
			reviews.add(new ProductCatalogReview(review));
		}

		for (Category category : product.getCategories()) {
			categories.add(new ProductCatalogCategory(category));
		}
	}

	@Data
	@NoArgsConstructor
	public static class ProductCatalogReview {
		private Long reviewId;
		private Long rating;
		private String comment;
		private ProductCatalogCategory categories;

		public ProductCatalogReview(Review review) {
			reviewId = review.getReviewId();
			rating = review.getRating();
			comment = review.getComment();
		}
	}

	@Data
	@NoArgsConstructor
	public static class ProductCatalogCategory {
		private Long categoryId;
		private String type;

		public ProductCatalogCategory(Category category) {
			categoryId = category.getCategoryId();
			type = category.getType();
		}
	}
}
