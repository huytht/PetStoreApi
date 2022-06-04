 package com.hcmue.service;

import java.util.List;

import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.product.PetProductDto;
import com.hcmue.dto.product.ProductCreate;
import com.hcmue.provider.file.UnsupportedFileTypeException;

public interface PetProductService {
	
	AppServiceResult<List<PetProductDto>> getPetProducts();
	
	AppServiceResult<PetProductDto> getPetProductDetail(Long petProductId);
	
	AppServiceResult<PetProductDto> addPetProduct(ProductCreate product) throws UnsupportedFileTypeException;
}
