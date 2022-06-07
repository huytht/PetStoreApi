package com.hcmue.dto.product;

import java.util.ArrayList;
import java.util.List;

import com.hcmue.dto.category.CategoryDto;
import com.hcmue.entity.PetProduct;
import com.hcmue.entity.ProductImages;

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

	private List<String> imagePath = new ArrayList<String>();
	
	private Boolean status;

	private CategoryDto category;
	
	public static PetProductDto CreateFromEntity(PetProduct src) {
		PetProductDto petProductDto = new PetProductDto();

		petProductDto.id = src.getId();
		petProductDto.name = src.getName();
		petProductDto.amount = src.getAmount();
		petProductDto.description = src.getDescription();
		if (src.getProductImages() != null) 
			for (ProductImages image : src.getProductImages()) {
				petProductDto.imagePath.add(image.getImagePath());
			}
		petProductDto.status = src.getStatus();
		petProductDto.category = CategoryDto.CreateFromEntity(src.getCategory());		

		return petProductDto;
	}
}
