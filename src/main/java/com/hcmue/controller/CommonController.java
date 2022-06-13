package com.hcmue.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.HttpResponse;
import com.hcmue.dto.HttpResponseError;
import com.hcmue.dto.HttpResponseSuccess;
import com.hcmue.dto.breed.BreedDto;
import com.hcmue.dto.category.CategoryDto;
import com.hcmue.dto.product.ProductDto;
import com.hcmue.entity.Breed;
import com.hcmue.service.CommonService;

@RestController
@RequestMapping("/common")
public class CommonController {

	private EntityManager entityManager;
	private CommonService commonService;

	@Autowired
	public CommonController(EntityManager entityManager, CommonService commonService) {
		this.entityManager = entityManager;
		this.commonService = commonService;
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
	
	@GetMapping(path = "/list/breed")
	public ResponseEntity<HttpResponse> getListBreedByCategory(@Valid @RequestParam(value = "category-id", required = false, defaultValue = "0") Long categoryId) {
		
		AppServiceResult<List<Breed>> result = commonService.getAllBreedByCategory(categoryId);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<Breed>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@GetMapping(path = "/list/category")
	public ResponseEntity<HttpResponse> getListCategory() {
		
		AppServiceResult<List<CategoryDto>> result = commonService.getCategoryList();

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<CategoryDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
}