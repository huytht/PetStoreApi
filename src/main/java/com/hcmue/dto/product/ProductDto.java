package com.hcmue.dto.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hcmue.dto.breed.BreedDto;
import com.hcmue.dto.category.CategoryDto;
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
public class ProductDto {
	
	private Long id;

	private String name;
	
	private Integer age;
	
	private Long amount;

	private List<OriginDto> origins = new ArrayList<OriginDto>();
	
	private String description;

	private List<String> imagePath = new ArrayList<String>();
	
	private Boolean gender;
	
	private BigDecimal price;
	
	private Boolean status;

	private BreedDto breed;
	
	private CategoryDto category;
	
	public static ProductDto CreateFromEntity(Product src) {
		ProductDto petDto = new ProductDto();

		petDto.id = src.getId();
		petDto.name = src.getName();
		petDto.amount = src.getAmount();
		if (src.getOrigins() != null)
			for (Origin origin : src.getOrigins()) {
				petDto.origins.add(OriginDto.CreateFromEntity(origin));
			}
		petDto.description = src.getDescription();

		if (src.getProductImages() != null) 
			for (ProductImages image : src.getProductImages()) {
				petDto.imagePath.add(image.getImagePath());
			}
		
		petDto.status = src.getStatus();
		
		petDto.price = src.getPrice();
		
		if (src.getAge() != null)
			petDto.age = src.getAge();
		
		if (src.getGender() != null) 
			petDto.gender = src.getGender();
		
		if (src.getBreed() != null)
			petDto.breed = BreedDto.CreateFromEntity(src.getBreed());
		
		petDto.category = CategoryDto.CreateFromEntity(src.getCategory());

		return petDto;
	}
}
