package com.hcmue.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hcmue.constant.AppError;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.product.PetProductDto;
import com.hcmue.entity.PetProduct;
import com.hcmue.provider.file.FileService;
import com.hcmue.repository.PetProductRepository;
import com.hcmue.service.PetProductService;

public class PetProductServiceImpl implements PetProductService{

	private final Logger logger = LoggerFactory.getLogger(PetProductServiceImpl.class);
	
	
	private PetProductRepository petProductRepository;
	private FileService imageFileService;
	
	public PetProductServiceImpl(PetProductRepository petProductRepository, FileService imageFileService) {
		this.petProductRepository = petProductRepository;
		this.imageFileService = imageFileService;
	}

	@Override
	public AppServiceResult<List<PetProductDto>> getPetProducts() {
		try {
			List<PetProduct> petProducts = petProductRepository.findAll();
			List<PetProductDto> result = new ArrayList<PetProductDto>();
			
			if (petProducts != null && petProducts.size() > 0)
				petProducts.forEach(item -> result.add(PetProductDto.CreateFromEntity(item)));
			
			return new AppServiceResult<List<PetProductDto>>(true, 0, "Succeed!", result);
		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<PetProductDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<PetProductDto> getPetProductDetail(Long petProductId) {
		try {
			PetProduct petProduct = petProductRepository.findById(petProductId).orElse(null);

			return petProduct == null
					? new AppServiceResult<PetProductDto>(false, AppError.Validattion.errorCode(),
							"Pet id is not exist: " + petProductId, null)
					: new AppServiceResult<PetProductDto>(true, 0, "Succeed!", PetProductDto.CreateFromEntity(petProduct));

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<PetProductDto>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

}
