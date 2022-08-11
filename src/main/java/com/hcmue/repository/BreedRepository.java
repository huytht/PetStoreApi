package com.hcmue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcmue.entity.Breed;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {	
	Breed findByName(String name);
}
