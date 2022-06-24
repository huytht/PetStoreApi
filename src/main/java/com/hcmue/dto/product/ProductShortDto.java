package com.hcmue.dto.product;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;

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
public class ProductShortDto {
	
	private Long id;

	private String name;
	
	private String imagePath;
	
	private Integer rate;
	
	private BigDecimal price;
	
	private Long amount;
	
	public static ProductShortDto CreateFromEntity(Product src) {
		ProductShortDto productDto = new ProductShortDto();

		productDto.id = src.getId();
		productDto.name = src.getName();
		productDto.rate = src.getRate();
		productDto.price = src.getPrice();
		productDto.amount = src.getAmount();
		
		if (!src.getProductImages().isEmpty())  {
			Collections.sort(src.getProductImages(), Comparator.comparingLong(ProductImages::getId));
			productDto.imagePath = src.getProductImages().iterator().next().getImagePath();
		}
		
		return productDto;
	}
}
