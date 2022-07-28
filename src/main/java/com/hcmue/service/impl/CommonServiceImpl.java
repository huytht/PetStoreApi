package com.hcmue.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmue.constant.AppError;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.breed.BreedDto;
import com.hcmue.dto.category.CategoryDto;
import com.hcmue.dto.order.OrderStatusDto;
import com.hcmue.entity.Breed;
import com.hcmue.entity.Category;
import com.hcmue.entity.OrderStatus;
import com.hcmue.repository.BreedRepository;
import com.hcmue.repository.CategoryRepository;
import com.hcmue.repository.OrderStatusRepository;
import com.hcmue.repository.ProductRepository;
import com.hcmue.service.CommonService;

@Service
public class CommonServiceImpl implements CommonService{
	
	private final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);
	
	private ProductRepository productRepository;
	private CategoryRepository categoryRepository;
	private BreedRepository breedRepository;
	private OrderStatusRepository orderStatusRepository;
	
	@Autowired
	public CommonServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
			BreedRepository breedRepository, OrderStatusRepository orderStatusRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.breedRepository = breedRepository;
		this.orderStatusRepository = orderStatusRepository;
	};
	

	@Override
	public AppServiceResult<List<Breed>> getAllBreedByCategory(Long categoryId) {
		try {
			List<Breed> result = new ArrayList<Breed>();
			
			if (categoryId != 0) {
				Category category = categoryRepository.findById(categoryId).orElse(null);
				
				if (category == null) {
					logger.warn("Category Id: " + categoryId + " is not exist!");
				
					return new AppServiceResult<List<Breed>>(false, AppError.Validattion.errorCode(),
							"Category Id: " + categoryId + " is not exist!", null);
				}	
				result = productRepository.findAllBreedByCategoryId(categoryId);
			} else {
				result = breedRepository.findAll();
			}
			
			return new AppServiceResult<List<Breed>>(true, 0, "Succeed!", result);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<Breed>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<List<CategoryDto>> getCategoryList() {
		
		try {
			List<Category> categories = categoryRepository.findAll();
			List<CategoryDto> result = new ArrayList<CategoryDto>();
			
			if (categories != null && categories.size() > 0) 
				categories.forEach(item -> result.add(CategoryDto.CreateFromEntity(item)));
			
			return new AppServiceResult<List<CategoryDto>>(true, 0, "Succeed!", result);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<CategoryDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
		
	}


	@Override
	public AppServiceResult<List<OrderStatusDto>> getOrderStatusList() {
		
		try {
			List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
			List<OrderStatusDto> result = new ArrayList<OrderStatusDto>();
			
			if (orderStatusList != null && orderStatusList.size() > 0)
				orderStatusList.forEach(item -> result.add(OrderStatusDto.CreateFromEntity(item)));
			
			return new AppServiceResult<List<OrderStatusDto>>(true, 0, "Succeed!", result);
		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<OrderStatusDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

}
