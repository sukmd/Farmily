package com.ssafy.farmily.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.farmily.dto.FamilyBasketDto;
import com.ssafy.farmily.dto.FamilyItemDto;
import com.ssafy.farmily.dto.FamilyMainDto;
import com.ssafy.farmily.dto.FamilyMemberResponseDto;
import com.ssafy.farmily.dto.JoinRequestDto;
import com.ssafy.farmily.dto.MakingFamilyRequestDto;
import com.ssafy.farmily.dto.PlacingItemRequestDto;
import com.ssafy.farmily.dto.RafflingRequestDto;
import com.ssafy.farmily.service.family.FamilyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/family")
@Slf4j
@RequiredArgsConstructor
public class FamilyController {
	private final FamilyService familyService;

	// 그래서 준비했다, lombok!!!

	// 요청 시 메인 인덱스에 DB에 저장된 가족정보를 가져옴

	@GetMapping("/{familyId}")
	@Operation(
		summary = "가족 메인 정보 조회",
		description = "메인에서 가족의 정보를 조회합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "조회 성공",
			content = @Content(schema = @Schema(implementation = FamilyMainDto.class))
		)
	})
	public ResponseEntity<FamilyMainDto> mainIndex(@PathVariable(value = "familyId") Long familyId) {
		FamilyMainDto familyMainDTO = familyService.setMainFamilyInfo(familyId);

		return ResponseEntity.ok(familyMainDTO);
	}

	@GetMapping("/{familyId}/inventory")
	@Operation(
		summary = "인벤토리 조회",
		description = "가족의 인벤토리를 조회합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "조회 성공",
			content = @Content(schema = @Schema(implementation = FamilyItemDto.class))
		)
	})
	public ResponseEntity<List<FamilyItemDto>> getInventory(@PathVariable(value = "familyId") Long familyId) {
		List<FamilyItemDto> familyItemDtoList = familyService.getFamilyInventory(familyId);

		return ResponseEntity.ok(familyItemDtoList);
	}

	@GetMapping("/{familyId}/basket")
	@Operation(
		summary = "스프린트 조회",
		description = "가족의 스프린트를 조회합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "조회 성공",
			content = @Content(schema = @Schema(implementation = FamilyBasketDto.class))
		)
	})
	public ResponseEntity<List<FamilyBasketDto>> getFamilyBasketList(@PathVariable(value = "familyId") Long familyId) {
		List<FamilyBasketDto> familyBasketDTOList = familyService.getFamilySprintList(familyId);

		return ResponseEntity.ok(familyBasketDTOList);
	}

	/*
	{
    "treeId":,
    "placementDtoList":[
        {
        "dtype":"",
        "position":{
            "row":,
            "col":
        },
        "recordId":
        }
    ]
}
	 */
	@PostMapping("/placement")
	@Operation(
		summary = "아이템 배치",
		description = "DB에 저장된 배치 배열을 제거하고 새로운 배열을 저장합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "배열 저장 성공"
		)
	})
	public ResponseEntity<Void> itemPlacement(@RequestBody PlacingItemRequestDto placementList) {
		familyService.placingItems(placementList);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/insertFamily")
	@Operation(
		summary = "가족 생성",
		description = "가족 이름과 가훈, 멤버 정보를 받아 가족을 생성합니다. 생성한 사람은 가장으로 설정됩니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "가족 생성 성공"
		)
	})
	public ResponseEntity<Void> createFamily(@RequestBody MakingFamilyRequestDto makingFamilyRequestDto, @AuthenticationPrincipal String username){
		familyService.makeFamily(makingFamilyRequestDto,username);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/refreshSprint")
	@Operation(
		summary = "스프린트 수확 및 새로고침",
		description = "스프린트를 수확하고 수확 시점을 startDate로 스프린트를 새고로침합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "스프린트 새로고침 성공"
		)
	})
	public ResponseEntity<Void> refreshSprint(@RequestBody Long familyId){
		familyService.swapSprint(familyId);
		return ResponseEntity.ok().build();
	}

	@Operation(
		summary = "가족 참가",
		description = "가족 초대코드를 입력하여 가족 참가"
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "가족 참가 성공"
		)
	})
	@PostMapping("/join")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Void> joinFamily(@RequestBody String invitationCode, @AuthenticationPrincipal String username){
		familyService.insertFamilyMemberShip(invitationCode, username);
		return ResponseEntity.ok().build();
	}

	// invitationCode를 가져오는 것
	@GetMapping("/{familyId}/getInvitationCode")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<String> getInvitationCode(@PathVariable(value = "familyId") Long familyId){
		String code = familyService.getInvitationCode(familyId);
		return ResponseEntity.ok(code);
	}

	// @PostMapping("/{familyId}/raffling")
	// @PreAuthorize("hasRole('ROLE_USER')")
	// public ResponseEntity<Long> raffleItem(@PathVariable Long famliyId){
	// 	Long rafflingItemId = familyService.
	// 	return ResponseEntity<rafflingItemId>.isOK().build();
	// }

	@Operation(
		summary = "가족 구성원 리스트 가져오기",
		description = "가족 구성원들의 정보를 받아옴"
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "리스트 가져오기 성공"
		)
	})
	@GetMapping("/{familyId}/familyMembers")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<FamilyMemberResponseDto>> loadFamilyMemberList(
		@PathVariable Long familyId,
		@AuthenticationPrincipal String username){
		List<FamilyMemberResponseDto> familyMemberList = familyService.loadFamilyMemberList(familyId,username);
		return ResponseEntity.ok(familyMemberList);
	}

	@Operation(
		summary = "가장 위임",
		description = "가족ID와 위임 받을 사람의 ID 가장을 위임합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "가장 위임 성공"
		)
	})
	@PutMapping("/{familyId}/mandate/{memberId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Void> mandateHead(
		@PathVariable(value = "familyId") Long familyId,
		@PathVariable(value = "memberId") Long memberId,
		@AuthenticationPrincipal String username){
		familyService.mandateHead(familyId,memberId,username);
		return ResponseEntity.ok().build();
	}
}
