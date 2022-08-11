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

	private Long id;
	
	private String name;

	public static CategoryDto CreateFromEntity(Category src) {
		CategoryDto dto = new CategoryDto();

		dto.id = src.getId();
		dto.name = src.getName();

		return dto;
	}
}
