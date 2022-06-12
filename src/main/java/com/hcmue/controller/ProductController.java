package com.hcmue.controller;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hcmue.provider.file.UnsupportedFileTypeException;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.HttpResponse;
import com.hcmue.dto.HttpResponseError;
import com.hcmue.dto.HttpResponseSuccess;
import com.hcmue.dto.pagination.PageDto;
import com.hcmue.dto.pagination.PageParam;
import com.hcmue.dto.product.ProductDto;
import com.hcmue.dto.user.RemarkProduct;
import com.hcmue.dto.product.ProductCreate;
import com.hcmue.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
	
	private ProductService productService;
	
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<HttpResponse> getProductListByType(@RequestParam(name="type") String type,
			@RequestParam(name = "breed-id", required = false, defaultValue = "0") Long breedId,
			@RequestParam(name = "category-id", required = false, defaultValue = "-1") Long categoryId,
			@RequestParam(name = "page-number", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "page-size", required = false, defaultValue = "30") int pageSize) {
		
		PageParam pageParam = new PageParam();
		pageParam.setPageIndex(pageNumber);
		pageParam.setPageSize(pageSize);
		
		AppServiceResult<PageDto<ProductDto>> result = breedId != 0
				? productService.getPetListByBreed(type, breedId, pageParam)
				: categoryId != -1
				? productService.getProductListByCategory(type, categoryId, pageParam) 
				: productService.getProductListByType(type, pageParam);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<PageDto<ProductDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@GetMapping("/detail")
	public ResponseEntity<HttpResponse> getProductDetail(@Valid @RequestParam(value = "id") Long productId) {

		AppServiceResult<ProductDto> result = productService.getProductDetail(productId);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<ProductDto>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@PostMapping
	public ResponseEntity<HttpResponse> addProduct(@RequestParam(value = "name") String name,
			@RequestParam(value = "amount") Long amount,
			@RequestParam(value = "description") String description,
			@RequestParam(value = "imageFiles") MultipartFile[] imageFile,
			@RequestParam(value = "gender", required = false) Boolean gender,
			@RequestParam(value = "status") Boolean status,
			@RequestParam(value = "breedId", required = false) Long breedId,
			@RequestParam(value = "categoryId") Long categoryId,
			@RequestParam(value = "price") BigDecimal price,
			@RequestParam(value = "age", required = false) Integer age,
			@RequestParam(value = "originIds", required = false) Long[] originIds)
			throws UnsupportedFileTypeException {

		ProductCreate newProduct = new ProductCreate(name, amount, description, imageFile, gender, age, status, breedId, originIds, categoryId, price);

		AppServiceResult<ProductDto> result = productService.addProduct(newProduct);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<ProductDto>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@GetMapping("/remark/list")
	public ResponseEntity<HttpResponse> getListRemark(@Valid @RequestParam(value="id") Long productId,
			@RequestParam(name = "page-number", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "page-size", required = false, defaultValue = "30") int pageSize) {
		
		PageParam pageParam = new PageParam();
		pageParam.setPageIndex(pageNumber);
		pageParam.setPageSize(pageSize);
		
		AppServiceResult<PageDto<RemarkProduct>> result = productService.getRemarkListByProduct(productId, pageParam);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<PageDto<RemarkProduct>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	
}
