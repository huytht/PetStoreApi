package com.hcmue.controller;

import javax.persistence.EntityManager;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmue.dto.HttpResponse;
import com.hcmue.dto.HttpResponseSuccess;

@RestController
@RequestMapping("/common")
public class CommonController {

	private EntityManager entityManager;

	@Autowired
	public CommonController(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@GetMapping(path = "/update-fts")
	public ResponseEntity<HttpResponse> updateFts() {
		
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		
		try {
			fullTextEntityManager.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return ResponseEntity.ok(new HttpResponseSuccess<String>("Failed!"));
		}
		
		return ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!"));
	}
}
