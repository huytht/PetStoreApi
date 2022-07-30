package com.hcmue.dto.product;

import java.math.BigDecimal;

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
	
	private BigDecimal price;

	private Long breedId;
	
	private Long[] originIds;
	
	private Long categoryId;

	public ProductCreate(String name, Long amount, String description, @NotEmpty MultipartFile[] imageFile,
			Long categoryId, BigDecimal price) {
		this.name = name;
		this.amount = amount;
		this.description = description;
		this.imageFile = imageFile;
		this.categoryId = categoryId;
		this.price = price;
	}

	public ProductCreate(String name, Long amount, String description, @NotEmpty MultipartFile[] imageFile,
			Boolean gender, Integer age, Long breedId, Long[] originIds, Long categoryId, BigDecimal price) {
		this.name = name;
		this.amount = amount;
		this.description = description;
		this.imageFile = imageFile;
		this.gender = gender;
		this.age = age;
		this.breedId = breedId;
		this.originIds = originIds;
		this.categoryId = categoryId;
		this.price = price;
	}
	
}
