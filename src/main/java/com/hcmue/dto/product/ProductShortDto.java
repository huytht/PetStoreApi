package com.hcmue.dto.product;

import java.math.BigDecimal;

import com.hcmue.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductShortDto {
	
	private Long id;

	private String name;
	
	private String imagePath;
	
	private Integer rate;
	
	private BigDecimal price;
	
	public static ProductShortDto CreateFromEntity(Product src) {
		ProductShortDto petDto = new ProductShortDto();

		petDto.id = src.getId();
		petDto.name = src.getName();
		petDto.rate = src.getRate();
		petDto.price = src.getPrice();
		
		if (!src.getProductImages().isEmpty()) 
			petDto.imagePath = src.getProductImages().iterator().next().getImagePath();
		
		return petDto;
	}
}
