package com.hcmue.dto.product;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreate {
	
	private String name;
	
	private Long amount;
	
	private String description;

	@NotEmpty
	private MultipartFile[] imageFile;
	
	private Boolean gender;
	
	private Integer age;
	
	private Boolean status;

	private Long breedId;
	
	private Long[] originIds;
	
	private Long categoryId;

	public ProductCreate(String name, Long amount, String description, @NotEmpty MultipartFile[] imageFile,
			Boolean status, Long categoryId) {
		this.name = name;
		this.amount = amount;
		this.description = description;
		this.imageFile = imageFile;
		this.status = status;
		this.categoryId = categoryId;
	}

	public ProductCreate(String name, Long amount, String description, @NotEmpty MultipartFile[] imageFile,
			Boolean gender, Integer age, Boolean status, Long breedId, Long[] originIds, Long categoryId) {
		this.name = name;
		this.amount = amount;
		this.description = description;
		this.imageFile = imageFile;
		this.gender = gender;
		this.age = age;
		this.status = status;
		this.breedId = breedId;
		this.originIds = originIds;
		this.categoryId = categoryId;
	}
	
}
