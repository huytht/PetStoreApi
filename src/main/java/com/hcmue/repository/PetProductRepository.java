package com.hcmue.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcmue.entity.PetProduct;

@Repository
public interface PetProductRepository extends JpaRepository<PetProduct, Long> {	
	public PetProduct findByName(String name);
	
	public Page<PetProduct> findAllByOrderByIdDesc(Pageable pageable);
}
