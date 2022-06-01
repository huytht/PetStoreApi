package com.hcmue.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hcmue.constant.AppError;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.product.PetDto;
import com.hcmue.entity.Pet;
import com.hcmue.provider.file.FileService;
import com.hcmue.repository.PetRepository;
import com.hcmue.service.PetService;

public class PetServiceImpl implements PetService{

	private final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);
	
	
	private PetRepository petRepository;
	private FileService imageFileService;
	
	public PetServiceImpl(PetRepository petRepository, FileService imageFileService) {
		this.petRepository = petRepository;
		this.imageFileService = imageFileService;
	}

	@Override
	public AppServiceResult<List<PetDto>> getPets() {
		try {
			List<Pet> pets = petRepository.findAll();
			List<PetDto> result = new ArrayList<PetDto>();
			
			if (pets != null && pets.size() > 0)
				pets.forEach(item -> result.add(PetDto.CreateFromEntity(item)));
			
			return new AppServiceResult<List<PetDto>>(true, 0, "Succeed!", result);
		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<PetDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<PetDto> getPetDetail(Long petId) {
		try {
			Pet pet = petRepository.findById(petId).orElse(null);

			return pet == null
					? new AppServiceResult<PetDto>(false, AppError.Validattion.errorCode(),
							"Pet id is not exist: " + petId, null)
					: new AppServiceResult<PetDto>(true, 0, "Succeed!", PetDto.CreateFromEntity(pet));

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<PetDto>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

}
