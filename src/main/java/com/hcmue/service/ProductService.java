 package com.hcmue.service;

import java.util.List;

import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.pagination.PageDto;
import com.hcmue.dto.pagination.PageParam;
import com.hcmue.dto.product.ProductDto;
import com.hcmue.dto.product.ProductCreate;
import com.hcmue.provider.file.UnsupportedFileTypeException;

public interface ProductService {
	
	AppServiceResult<List<ProductDto>> getProducts();
	
	AppServiceResult<ProductDto> getProductDetail(Long productId);
	
	AppServiceResult<PageDto<ProductDto>> getProductListByType(String typeOfProduct, PageParam pageParam);
	
	AppServiceResult<PageDto<ProductDto>> getPetListByBreed(String typeOfProduct, Long breedId, PageParam pageParam);
	
	AppServiceResult<ProductDto> addProduct(ProductCreate product) throws UnsupportedFileTypeException;
	
	AppServiceResult<PageDto<ProductDto>> getProductListByCategory(String typeOfProduct, Long categoryId, PageParam pageParam);
}
