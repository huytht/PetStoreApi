package com.hcmue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcmue.entity.Origin;

@Repository
public interface OriginRepository extends JpaRepository<Origin, Long> {	
	Origin findByName(String name);
}
