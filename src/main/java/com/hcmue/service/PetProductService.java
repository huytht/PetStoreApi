 package com.hcmue.service;

import java.util.List;

import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.product.PetProductDto;

public interface PetProductService {
	
	AppServiceResult<List<PetProductDto>> getPetProducts();
	
	AppServiceResult<PetProductDto> getPetProductDetail(Long petProductId);
}
