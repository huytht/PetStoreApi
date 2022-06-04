package com.hcmue.dto.breed;

import com.hcmue.dto.category.CategoryDto;
import com.hcmue.entity.Breed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BreedDto {

	private Long id;

	private String name;

	private CategoryDto categoryDto;

	public static BreedDto CreateFromEntity(Breed src) {
		BreedDto dto = new BreedDto();

		dto.id = src.getId();
		dto.name = src.getName();
		dto.categoryDto = CategoryDto.CreateFromEntity(src.getCategory());

		return dto;
	}
}
