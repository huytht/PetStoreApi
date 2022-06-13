package com.hcmue.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hcmue.entity.Breed;
import com.hcmue.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {	
	public Product findByName(String name);
	
	public Page<Product> findAllByOrderByIdDesc(Pageable pageable);
	
	public Page<Product> findAllByBreedId(Long breedId, Pageable pageable);
	
	public Page<Product> findAllByBreedIdOrderByIdDesc(Long breedId, Pageable pageable);
	
	public Page<Product> findAllByCategoryId(Long categoryId, Pageable pageable);
	
	public Page<Product> findAllByCategoryIdOrderByIdDesc(Long categoryId, Pageable pageable);
	
	@Transactional
	@Query(value = "SELECT DISTINCT p.breed FROM Product p WHERE p.category.id = :categoryId")
	public List<Breed> findAllBreedByCategoryId(Long categoryId);
	
	@Transactional
	@Query(value = "SELECT p FROM Product p WHERE p.category.id > 2")
	public Page<Product>findAllProductExceptPet(Pageable pageable);
}
