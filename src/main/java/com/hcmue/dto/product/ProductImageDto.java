package com.hcmue.dto.product;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import com.hcmue.dto.breed.BreedDto;
import com.hcmue.dto.origin.OriginDto;
import com.hcmue.entity.Origin;
import com.hcmue.entity.Pet;
import com.hcmue.entity.ProductImages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDto {
	
	private String imagePath;
	
	private PetDto petDto;
	
	private PetProductDto petProductDto;
	
	public static ProductImageDto CreateFromEntity(ProductImages src) {
		ProductImageDto dto = new ProductImageDto();

		if (src.getPet() != null)
			dto.petDto = PetDto.CreateFromEntity(src.getPet());

		if (src.getProduct() != null) 
			dto.petProductDto = PetProductDto.CreateFromEntity(src.getProduct());
		
		dto.imagePath = src.getImagePath();
		
		return dto;
	}
	
}
