 package com.hcmue.service;

import java.util.List;

import com.hcmue.domain.AppBaseResult;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.domain.FullTextSearchWithPagingParam;
import com.hcmue.dto.pagination.PageDto;
import com.hcmue.dto.pagination.PageParam;
import com.hcmue.dto.product.ProductDto;
import com.hcmue.dto.product.ProductShortDto;
import com.hcmue.dto.product.ProductUpdate;
import com.hcmue.dto.user.RemarkProduct;
import com.hcmue.dto.product.ProductCreate;
import com.hcmue.provider.file.UnsupportedFileTypeException;

public interface ProductService {
	
	AppServiceResult<List<ProductDto>> getProducts();
	
	AppServiceResult<ProductDto> getProductDetail(Long productId);
	
	AppServiceResult<PageDto<ProductShortDto>> getProductListByType(String typeOfProduct, PageParam pageParam);
	
	AppServiceResult<PageDto<ProductShortDto>> getPetListByBreed(String typeOfProduct, Long breedId, PageParam pageParam);
	
	AppServiceResult<ProductDto> addProduct(ProductCreate product) throws UnsupportedFileTypeException;
	
	AppServiceResult<PageDto<ProductShortDto>> getProductListByCategory(String typeOfProduct, Long categoryId, PageParam pageParam);
	
	AppServiceResult<PageDto<RemarkProduct>> getRemarkListByProduct(Long productId, PageParam pageParam);
	
	AppServiceResult<PageDto<ProductShortDto>> searchByFTS(FullTextSearchWithPagingParam params);
	
	AppBaseResult updateWishList(Long productId);
	
	AppServiceResult<PageDto<ProductShortDto>> getWishList(PageParam pageParam);
	
	AppServiceResult<List<ProductDto>> getCatList();
	
	AppServiceResult<List<ProductDto>> getDogList();
	
	AppServiceResult<List<ProductDto>> getProductList();
	
	AppBaseResult updateAmountInInventory(Long productId, Long amount);
	
	AppBaseResult updateProduct(Long productId, ProductUpdate product) throws UnsupportedFileTypeException;
	
	AppBaseResult deleteProduct(Long productId);
}
