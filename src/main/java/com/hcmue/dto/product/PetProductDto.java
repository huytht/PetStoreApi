package com.hcmue.dto.product;

import com.hcmue.dto.category.CategoryDto;
import com.hcmue.entity.PetProduct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetProductDto {
	
	private Long id;

	private String name;
	
	private Long amount;
	
	private String description;

	private String imagePath;
	
	private Boolean status;

	private CategoryDto category;
	
	public static PetProductDto CreateFromEntity(PetProduct src) {
		PetProductDto petProductDto = new PetProductDto();

		petProductDto.id = src.getId();
		petProductDto.name = src.getName();
		petProductDto.amount = src.getAmount();
		petProductDto.description = src.getDescription();
		petProductDto.imagePath = src.getImagePath();
		petProductDto.status = src.getStatus();
		petProductDto.category = CategoryDto.CreateFromEntity(src.getCategory());		

		return petProductDto;
	}
}
