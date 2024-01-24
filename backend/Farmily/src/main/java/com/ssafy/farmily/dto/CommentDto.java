package com.ssafy.farmily.dto;

import org.springframework.beans.BeanUtils;

import com.ssafy.farmily.entity.Comment;
import com.ssafy.farmily.entity.Record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "댓글 DTO")
public class CommentDto {
	private Long id;
	private String content;
	private MemberBasicDto author;

	public static CommentDto from(Comment entity) {
		CommentDto dto = new CommentDto();
		BeanUtils.copyProperties(entity, dto);
		return dto;
	}
}