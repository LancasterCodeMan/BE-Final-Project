package product.catalog.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import product.catalog.entity.Category;

public interface CategoryDao extends JpaRepository<Category, Long> {
}
