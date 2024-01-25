package com.ssafy.farmily.service.sprint;

import java.util.List;

import com.ssafy.farmily.dto.RecordBriefResponseDto;
import com.ssafy.farmily.entity.Sprint;

public interface SprintService {
	Sprint getEntityById(Long sprintId);

	void harvest(Long sprintId);

	List<RecordBriefResponseDto> getRecords(Long sprintId);
}
