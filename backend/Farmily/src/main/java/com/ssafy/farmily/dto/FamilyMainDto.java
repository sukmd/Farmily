package com.ssafy.farmily.dto;

import java.util.List;

import com.ssafy.farmily.entity.Family;
import com.ssafy.farmily.entity.Tree;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FamilyMainDto {
	Long id;
	String name;
	String motto;
	FamilyMainTreeDto tree;
	List<Long> challengesIds;
	MainSprintResponseDto mainSprint;
	String invitationCode;
	int point;
	public static FamilyMainDto of(Family family) {
		FamilyMainDto familyMainDTO = new FamilyMainDto();
		familyMainDTO.setId(family.getId());
		familyMainDTO.setName(family.getName());
		familyMainDTO.setMotto(family.getMotto());
		familyMainDTO.setInvitationCode(family.getInvitationCode());
		Tree tree = family.getTree();
		familyMainDTO.setTree(FamilyMainTreeDto.from(tree));
		familyMainDTO.setPoint(family.getPoint());
		return familyMainDTO;
	}

}
