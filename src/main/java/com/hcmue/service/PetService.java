 package com.hcmue.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.product.PetDto;

public interface PetService {
	
	AppServiceResult<List<PetDto>> getPets();
	
	AppServiceResult<PetDto> getPetDetail(Long petId);
}
