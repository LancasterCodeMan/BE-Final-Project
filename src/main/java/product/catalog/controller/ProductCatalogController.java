package product.catalog.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import product.catalog.controller.model.ProductCatalogData;
import product.catalog.controller.model.ProductCatalogData.ProductCatalogCategory;
import product.catalog.controller.model.ProductCatalogData.ProductCatalogReview;
import product.catalog.service.ProductCatalogService;

@RestController
@RequestMapping("/product_catalog")
@Slf4j
public class ProductCatalogController {
	
	@Autowired
	private ProductCatalogService productCatalogService;
	
	//Create a new product
	@PostMapping("/product")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ProductCatalogData createProduct(@RequestBody ProductCatalogData productCatalogData) {
		log.info("Recieved request to create a new Product with data: {}", productCatalogData);
		return productCatalogService.saveProduct(productCatalogData);
	}
	
	//Update a new product
	@PutMapping("product/{productId}")
	public ProductCatalogData updateProduct(@PathVariable Long productId, @RequestBody ProductCatalogData productCatalogData) {
		productCatalogData.setProductId(productId);
		log.info("Updating Product {}", productCatalogData);
		return productCatalogService.saveProduct(productCatalogData);
	}
	
	//Add Review to product
	@PostMapping("/product/{productId}/review")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ProductCatalogReview addReview(@PathVariable Long productId, @RequestBody ProductCatalogReview review) {
		log.info("Adding review {} for Product wtih ID={}", review, productId);
		return productCatalogService.saveReview(productId, review);
	}
	
	//Add Category
	@PostMapping("/category")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ProductCatalogCategory createCategory(@RequestBody ProductCatalogCategory productCatalogCategory) {
		log.info("Recieved request to create a new Category with data: {}", productCatalogCategory);
		return productCatalogService.saveCategory(productCatalogCategory);	
	}
	
	//Create Category and Add to Product
	@PostMapping("product/{productId}/category")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ProductCatalogCategory addCategory(@PathVariable Long productId, @RequestBody ProductCatalogCategory category) {
		log.info("Adding category {} for product with ID={}", category, productId);
		return productCatalogService.saveProductCategory(productId, category);
	}
	
	//Retrieve All Products (only products)
	@GetMapping
	public List<ProductCatalogData> retrieveAllProducts(){
		log.info("Retrieve all Products called.");
		return productCatalogService.retrieveAllProducts();
	}
	
	//Retrieve Product By Id with Categories and Reviews
	@GetMapping("product/{productId}")
	public ProductCatalogData retrieveProductById(@PathVariable Long productId) {
		log.info("Retrieving Product with ID={}", productId);
		return productCatalogService.retrieveProductById(productId);
	}
	
	//Delete Product By Id Including Reviews
	@DeleteMapping("product/{productId}")
	public Map<String, String> deleteProductById(@PathVariable Long productId){
		log.info("Deleting Product with ID={}", productId);
		productCatalogService.deleteProductById(productId);
		return Map.of("message", "Delettion of Product with ID=" + productId + " was successful." );
	}
	
	// Delete Category
	@DeleteMapping("/category/{categoryId}")
	public Map<String, String> deleteCategoryById(@PathVariable Long categoryId) {
	    log.info("Deleting category with ID={}", categoryId);
	    productCatalogService.deleteCategoryById(categoryId);
	    return Map.of("message", "Delettion of Product with ID=" + categoryId + " was successful." );
	}
	
	

}
