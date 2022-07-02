package com.hcmue.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hcmue.dto.user.RemarkProduct;
import com.hcmue.entity.*;
import com.hcmue.entity.Product;

@Repository
public interface AppUserProductRepository extends JpaRepository<AppUserProduct, Long> {
	
	public boolean existsByAppUserAndProduct(AppUser appUser, Product product);

	@Query(value = "SELECT app FROM AppUserProduct app WHERE app.product.id = :productId ORDER BY app.dateEdit DESC")
	public Page<AppUserProduct> findAllByProductIdOrderByDateUpdate(Long productId, Pageable pageable);
	
	public AppUserProduct findByAppUserProductId(AppUserProductId id);
}
