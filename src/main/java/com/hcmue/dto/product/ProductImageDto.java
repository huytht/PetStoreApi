package com.hcmue.dto.product;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import com.hcmue.dto.breed.BreedDto;
import com.hcmue.dto.origin.OriginDto;
import com.hcmue.entity.Origin;
import com.hcmue.entity.Product;
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
	
	private Long id;
	
	private String imagePath;
	
	private ProductDto productDto;
	
	public static ProductImageDto CreateFromEntity(ProductImages src) {
		ProductImageDto dto = new ProductImageDto();

		if (src.getProduct() != null) 
			dto.productDto = ProductDto.CreateFromEntity(src.getProduct());
		
		dto.imagePath = src.getImagePath();
		
		return dto;
	}
	
}
