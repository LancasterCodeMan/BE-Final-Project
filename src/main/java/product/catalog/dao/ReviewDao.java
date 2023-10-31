package product.catalog.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import product.catalog.entity.Review;

public interface ReviewDao extends JpaRepository<Review, Long> {

}
