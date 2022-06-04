package com.hcmue.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmue.provider.file.MediaFile;
import com.hcmue.provider.file.UnsupportedFileTypeException;
import com.hcmue.provider.file.FileServiceFactory;
import com.hcmue.provider.file.FileType;
import com.hcmue.constant.AppError;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.product.PetDto;
import com.hcmue.dto.product.ProductCreate;
import com.hcmue.entity.Breed;
import com.hcmue.entity.Origin;
import com.hcmue.entity.Pet;
import com.hcmue.provider.file.FileService;
import com.hcmue.repository.BreedRepository;
import com.hcmue.repository.OriginRepository;
import com.hcmue.repository.PetRepository;
import com.hcmue.service.PetService;

@Service
public class PetServiceImpl implements PetService{

	private final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);
	
	private PetRepository petRepository;
	private BreedRepository breedRepository;
	private OriginRepository originRepository;
	private FileService imageFileService;
	
	@Autowired
	public PetServiceImpl(PetRepository petRepository, BreedRepository breedRepository,
			OriginRepository originRepository) {
		this.petRepository = petRepository;
		this.breedRepository = breedRepository;
		this.originRepository = originRepository;
		this.imageFileService = FileServiceFactory.getFileService(FileType.IMAGE);
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

	@Override
	public AppServiceResult<PetDto> addPet(ProductCreate pet) throws UnsupportedFileTypeException {
		
		try {
			Pet newPet = new Pet();
			newPet.setName(pet.getName());
			if (pet.getBreedId() != null) {
				Breed breed = breedRepository.findById(pet.getBreedId()).orElse(null);
				
				if (breed == null) {
					logger.warn("BreedId: " + pet.getBreedId() + " is not exist!");
					
					return new AppServiceResult<PetDto>(false, AppError.Validattion.errorCode(),
							"BreedId: " + pet.getBreedId() + " is not exist!", null);
				} else
					newPet.setBreed(breed);
			}
			
			newPet.setAmount(pet.getAmount());
			newPet.setDescription(pet.getDescription());
			newPet.setGender(pet.getGender());
			newPet.setStatus(pet.getStatus());
			
			if (pet.getImageFile() != null) {
				MediaFile mediaFile = imageFileService.upload(newPet.getName(), pet.getImageFile());
				newPet.setImagePath(mediaFile.getPathUrl());
			}
			
			for (Long originId : pet.getOriginIds()) {
				Origin origin = originRepository.findById(originId).orElse(null);
				if (origin == null) {
					logger.warn("Origin id is not exist: " + originId + ". Can not handle farther!");

					throw new IllegalArgumentException("Origin id is not exist: " + originId);
				} else
					newPet.getOrigins().add(origin);
			}
			
			petRepository.save(newPet);
			
			final PetDto dto = PetDto.CreateFromEntity(newPet);
			
			
			return new AppServiceResult<PetDto>(true, 0, "Succeed!", dto);
			
		} catch (UnsupportedFileTypeException e) {
			e.printStackTrace();

			throw e;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();

			return new AppServiceResult<PetDto>(false, AppError.Validattion.errorCode(), e.getMessage(), null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new AppServiceResult<PetDto>(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage(),
					null);
		}
	}

}
