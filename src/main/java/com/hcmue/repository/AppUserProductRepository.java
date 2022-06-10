package com.hcmue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcmue.entity.*;
import com.hcmue.entity.Product;

@Repository
public interface AppUserProductRepository extends JpaRepository<AppUserProduct, Long> {
	
	public boolean existsByAppUserAndProduct(AppUser appUser, Product product);
}
