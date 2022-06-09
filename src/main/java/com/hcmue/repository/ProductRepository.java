package com.hcmue.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcmue.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {	
	public Product findByName(String name);
	
	public Page<Product> findAllByOrderByIdDesc(Pageable pageable);
	
	public Page<Product> findAllByBreedId(Long breedId, Pageable pageable);
	
	public Page<Product> findAllByBreedIdOrderByIdDesc(Long breedId, Pageable pageable);
	
	public Page<Product> findAllByCategoryId(Long categoryId, Pageable pageable);
	
	public Page<Product> findAllByCategoryIdOrderByIdDesc(Long categoryId, Pageable pageable);
}
