package product.catalog.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import product.catalog.entity.Product;

public interface ProductDao extends JpaRepository<Product, Long> {

}
