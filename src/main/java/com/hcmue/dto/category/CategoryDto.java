package com.hcmue.dto.category;

import com.hcmue.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

	private String name;

	private String description;

	public static CategoryDto CreateFromEntity(Category src) {
		CategoryDto dto = new CategoryDto();

		dto.name = src.getName();
		dto.description = src.getDescription();

		return dto;
	}
}
