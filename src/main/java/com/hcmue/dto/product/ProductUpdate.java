package com.hcmue.dto.product;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductUpdate {
	
	private String name;
	
	private String description;

	private MultipartFile[] imageFile;
	
	private Boolean gender;
	
	private Integer age;
	
	private BigDecimal price;

	private Long breedId;
	
	private Long[] originIds;
	
	private Long categoryId;

	public ProductUpdate(String name, String description, MultipartFile[] imageFile,
			Long categoryId, BigDecimal price) {
		this.name = name;
		this.description = description;
		this.imageFile = imageFile;
		this.categoryId = categoryId;
		this.price = price;
	}

	public ProductUpdate(String name, String description, MultipartFile[] imageFile,
			Boolean gender, Integer age, Long breedId, Long[] originIds, Long categoryId, BigDecimal price) {
		this.name = name;
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
