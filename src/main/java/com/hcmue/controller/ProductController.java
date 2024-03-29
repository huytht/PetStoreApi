package com.hcmue.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hcmue.provider.file.UnsupportedFileTypeException;
import com.hcmue.domain.FullTextSearchWithPagingParam;
import com.hcmue.util.StringUtil;
import com.hcmue.domain.AppBaseResult;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.HttpResponse;
import com.hcmue.dto.HttpResponseError;
import com.hcmue.dto.HttpResponseSuccess;
import com.hcmue.dto.order.OrderDto;
import com.hcmue.dto.pagination.PageDto;
import com.hcmue.dto.pagination.PageParam;
import com.hcmue.dto.product.ProductDto;
import com.hcmue.dto.product.ProductShortDto;
import com.hcmue.dto.product.ProductUpdate;
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

	@GetMapping("/list")
	public ResponseEntity<HttpResponse> getProductListByType(@RequestParam(name="type") String type,
			@RequestParam(name = "breed-id", required = false, defaultValue = "0") Long breedId,
			@RequestParam(name = "category-id", required = false, defaultValue = "-1") Long categoryId,
			@RequestParam(name = "page-number", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "page-size", required = false, defaultValue = "30") int pageSize) {
		
		PageParam pageParam = new PageParam();
		pageParam.setPageIndex(pageNumber);
		pageParam.setPageSize(pageSize);
		
		AppServiceResult<PageDto<ProductShortDto>> result = breedId != 0
				? productService.getPetListByBreed(type, breedId, pageParam)
				: categoryId != -1
				? productService.getProductListByCategory(type, categoryId, pageParam) 
				: productService.getProductListByType(type, pageParam);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<PageDto<ProductShortDto>>(result.getData()))
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
			@RequestParam(value = "breedId", required = false) Long breedId,
			@RequestParam(value = "categoryId") Long categoryId,
			@RequestParam(value = "price") BigDecimal price,
			@RequestParam(value = "age", required = false) Integer age,
			@RequestParam(value = "originIds", required = false) Long[] originIds)
			throws UnsupportedFileTypeException {

		ProductCreate newProduct = new ProductCreate(name, amount, description, imageFile, gender, age, breedId, originIds, categoryId, price);

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
	
	@GetMapping(path = "/search-text")
	public ResponseEntity<HttpResponse> searchText(@RequestParam(value = "text", required = true) String text,
				@RequestParam(name = "page-number", required = false, defaultValue = "0") Integer pageNumber,
				@RequestParam(name = "page-size", required = false, defaultValue = "30") Integer pageSize) {
		
		if(StringUtil.isBlank(text))
			return ResponseEntity.badRequest().body(new HttpResponseError(null, "Text search is not empty"));
			
		FullTextSearchWithPagingParam params = new FullTextSearchWithPagingParam();
		params.getPageParam().setPageIndex(pageNumber == null ? 0 : pageNumber);
		params.getPageParam().setPageSize(pageSize == null ? 30 : pageSize);
		params.setText(text);
		
		AppServiceResult<PageDto<ProductShortDto>> result = productService.searchByFTS(params);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<PageDto<ProductShortDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@GetMapping("/list/cat")
	public ResponseEntity<HttpResponse> getCatList() {
		
		AppServiceResult<List<ProductDto>> result = productService.getCatList();
		
		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<ProductDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@GetMapping("/list/dog")
	public ResponseEntity<HttpResponse> getDogList() {
		
		AppServiceResult<List<ProductDto>> result = productService.getDogList();
		
		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<ProductDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@GetMapping("/list/product")
	public ResponseEntity<HttpResponse> getProductList() {
		
		AppServiceResult<List<ProductDto>> result = productService.getProductList();
		
		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<ProductDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@PutMapping("/amount")
	public ResponseEntity<HttpResponse> updateAmount(@RequestParam(name = "product-id") Long productId, @RequestParam(name = "amount") Long amount) {
		AppBaseResult result = productService.updateAmountInInventory(productId, amount);
		
		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@PutMapping
	public ResponseEntity<HttpResponse> update(@RequestParam(name = "product-id") Long productId, @RequestBody ProductUpdate productUpdate) throws UnsupportedFileTypeException {
		AppBaseResult result = productService.updateProduct(productId, productUpdate);
		
		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@DeleteMapping
	public ResponseEntity<HttpResponse> deleteProduct(@RequestParam(name = "product-id") Long productId) {
		AppBaseResult result = productService.deleteProduct(productId);
		
		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
}
