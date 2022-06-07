package com.hcmue.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hcmue.constant.AppError;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.product.PetProductDto;
import com.hcmue.dto.product.ProductCreate;
import com.hcmue.entity.Category;
import com.hcmue.entity.PetProduct;
import com.hcmue.entity.ProductImages;
import com.hcmue.provider.file.FileService;
import com.hcmue.provider.file.FileServiceFactory;
import com.hcmue.provider.file.FileType;
import com.hcmue.provider.file.MediaFile;
import com.hcmue.provider.file.UnsupportedFileTypeException;
import com.hcmue.repository.CategoryRepository;
import com.hcmue.repository.PetProductRepository;
import com.hcmue.service.PetProductService;

@Service
public class PetProductServiceImpl implements PetProductService{

	private final Logger logger = LoggerFactory.getLogger(PetProductServiceImpl.class);
	
	
	private PetProductRepository petProductRepository;
	private CategoryRepository categoryRepository;
	private FileService imageFileService;
	
	@Autowired
	public PetProductServiceImpl(PetProductRepository petProductRepository, CategoryRepository categoryRepository) {
		this.petProductRepository = petProductRepository;
		this.categoryRepository = categoryRepository;
		this.imageFileService = FileServiceFactory.getFileService(FileType.IMAGE);
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
	
	@Override
	public AppServiceResult<PetProductDto> addPetProduct(ProductCreate product) throws UnsupportedFileTypeException {
		
		try {
			PetProduct newProduct = new PetProduct();
			
			newProduct.setName(product.getName());
			
			if (product.getCategoryId() != null) {
				Category category = categoryRepository.findById(product.getCategoryId()).orElse(null);
				
				if (category == null) {
					logger.warn("Category Id: " + product.getCategoryId() + " is not exist!");
					
					return new AppServiceResult<PetProductDto>(false, AppError.Validattion.errorCode(),
							"Category Id: " + product.getCategoryId() + " is not exist!", null);
				} else
					newProduct.setCategory(category);
			}
			
			newProduct.setAmount(product.getAmount());
			newProduct.setDescription(product.getDescription());
			newProduct.setStatus(product.getStatus());
			
			if (product.getImageFile() != null) {
				for (MultipartFile file : product.getImageFile()) {
					MediaFile mediaFile = imageFileService.upload(newProduct.getName(), file);
					ProductImages productImages = new ProductImages();
					productImages.setImagePath(mediaFile.getPathUrl());
					productImages.setProduct(newProduct);
					newProduct.getProductImages().add(productImages);
				}
			}
			
			petProductRepository.save(newProduct);
			
			final PetProductDto dto = PetProductDto.CreateFromEntity(newProduct);
			
			
			return new AppServiceResult<PetProductDto>(true, 0, "Succeed!", dto);
			
		} catch (UnsupportedFileTypeException e) {
			e.printStackTrace();

			throw e;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();

			return new AppServiceResult<PetProductDto>(false, AppError.Validattion.errorCode(), e.getMessage(), null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new AppServiceResult<PetProductDto>(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage(),
					null);
		}
	}

}
