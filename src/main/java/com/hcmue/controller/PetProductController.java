package com.hcmue.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.HttpResponse;
import com.hcmue.dto.HttpResponseError;
import com.hcmue.dto.HttpResponseSuccess;
import com.hcmue.dto.product.PetDto;
import com.hcmue.dto.product.PetProductDto;
import com.hcmue.dto.product.ProductCreate;
import com.hcmue.provider.file.UnsupportedFileTypeException;
import com.hcmue.service.PetProductService;

@RestController
@RequestMapping("/pet-product")
public class PetProductController {
	
	private PetProductService petProductService;
	
	@Autowired
	public PetProductController(PetProductService petProductService) {
		this.petProductService = petProductService;
	}

	@GetMapping
	public ResponseEntity<HttpResponse> getPets() {

		AppServiceResult<List<PetProductDto>> result = petProductService.getPetProducts();

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<PetProductDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@GetMapping("/detail")
	public ResponseEntity<HttpResponse> getPetProductDetail(@Valid @RequestParam(value = "id") Long petProductId) {

		AppServiceResult<PetProductDto> result = petProductService.getPetProductDetail(petProductId);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<PetProductDto>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@PostMapping
	public ResponseEntity<HttpResponse> addPetProduct(@RequestParam(value = "name") String name,
			@RequestParam(value = "amount") Long amount,
			@RequestParam(value = "description") String description,
			@RequestParam(value = "imageFiles") MultipartFile[] imageFile,
			@RequestParam(value = "status") Boolean status,
			@RequestParam(value = "categoryId") Long categoryId)
			throws UnsupportedFileTypeException {

		ProductCreate newProduct = new ProductCreate(name, amount, description, imageFile, status, categoryId);

		AppServiceResult<PetProductDto> result = petProductService.addPetProduct(newProduct);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<PetProductDto>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	
}
