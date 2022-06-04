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

import com.hcmue.provider.file.UnsupportedFileTypeException;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.HttpResponse;
import com.hcmue.dto.HttpResponseError;
import com.hcmue.dto.HttpResponseSuccess;
import com.hcmue.dto.product.PetDto;
import com.hcmue.dto.product.ProductCreate;
import com.hcmue.service.PetService;

@RestController
@RequestMapping("/pet")
public class PetController {
	
	private PetService petService;
	
	@Autowired
	public PetController(PetService petService) {
		this.petService = petService;
	}

	@GetMapping
	public ResponseEntity<HttpResponse> getPets() {

		AppServiceResult<List<PetDto>> result = petService.getPets();

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<PetDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@GetMapping("/detail")
	public ResponseEntity<HttpResponse> getPetDetail(@Valid @RequestParam(value = "id") Long petId) {

		AppServiceResult<PetDto> result = petService.getPetDetail(petId);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<PetDto>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@PostMapping
	public ResponseEntity<HttpResponse> addPet(@RequestParam(value = "name") String name,
			@RequestParam(value = "amount") Long amount,
			@RequestParam(value = "description") String description,
			@RequestParam(value = "imageFile") MultipartFile imageFile,
			@RequestParam(value = "gender") Boolean gender,
			@RequestParam(value = "status") Boolean status,
			@RequestParam(value = "breedId") Long breedId,
			@RequestParam(value = "originIds") Long[] originIds)
			throws UnsupportedFileTypeException {

		ProductCreate newPet = new ProductCreate(name, amount, description, imageFile, gender, status, breedId, originIds);

		AppServiceResult<PetDto> result = petService.addPet(newPet);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<PetDto>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	
}
