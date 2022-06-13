package com.hcmue.service;

import java.util.List;

import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.category.CategoryDto;
import com.hcmue.entity.Breed;

public interface CommonService {
	
	AppServiceResult<List<Breed>> getAllBreedByCategory(Long categoryId);
	
	AppServiceResult<List<CategoryDto>> getCategoryList();
}
