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

	private String name;

	public static BreedDto CreateFromEntity(Breed src) {
		BreedDto dto = new BreedDto();

		dto.name = src.getName();

		return dto;
	}
}
