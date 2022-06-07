 package com.hcmue.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.pagination.PageDto;
import com.hcmue.dto.pagination.PageParam;
import com.hcmue.dto.product.PetDto;
import com.hcmue.dto.product.ProductCreate;
import com.hcmue.provider.file.UnsupportedFileTypeException;

public interface PetService {
	
	AppServiceResult<List<PetDto>> getPets();
	
	AppServiceResult<PetDto> getPetDetail(Long petId);
	
	AppServiceResult<PageDto<PetDto>> getPetListByType(String typeOfPet, PageParam pageParam);
	
	AppServiceResult<PetDto> addPet(ProductCreate pet) throws UnsupportedFileTypeException;
}
