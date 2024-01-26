package com.ssafy.farmily.dto;

import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.ssafy.farmily.entity.Image;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "이미지 DTO")
@Builder
public class ImageDto {
	private String location;
	private String originalFileName;


	public static ImageDto from(Image image) {
		if (Objects.isNull(image)) return null;

		ImageDto imageDto = new ImageDto();
		BeanUtils.copyProperties(image, imageDto);
		return imageDto;
	}

	public static ImageDto of(String location, String originalFileName) {
		return new ImageDto(location, originalFileName);
	}
}
