 package com.hcmue.service;

import java.util.List;

import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.product.PetDto;
import com.hcmue.dto.product.ProductCreate;
import com.hcmue.provider.file.UnsupportedFileTypeException;

public interface PetService {
	
	AppServiceResult<List<PetDto>> getPets();
	
	AppServiceResult<PetDto> getPetDetail(Long petId);
	
	AppServiceResult<PetDto> addPet(ProductCreate pet) throws UnsupportedFileTypeException;
}
