package com.hcmue.domain;

import com.hcmue.dto.pagination.PageParam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FullTextSearchWithPagingParam {
	private String text;
	private PageParam pageParam = new PageParam();
}
