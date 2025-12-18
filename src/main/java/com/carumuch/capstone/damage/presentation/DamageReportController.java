package com.carumuch.capstone.damage.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carumuch.capstone.common.presentation.dto.ApiResponse;
import com.carumuch.capstone.common.presentation.dto.PagingRequest;
import com.carumuch.capstone.common.presentation.dto.PagingResponse;
import com.carumuch.capstone.damage.application.DamageReportService;
import com.carumuch.capstone.damage.presentation.dto.request.report.RegisterDamageReportRequest;
import com.carumuch.capstone.damage.presentation.dto.request.report.UpdateDamageReportRequest;
import com.carumuch.capstone.damage.presentation.dto.response.report.DamageReportInfoResponse;
import com.carumuch.capstone.identity.domain.user.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/damage-reports")
@RequiredArgsConstructor
public class DamageReportController {

	private final DamageReportService damageReportService;

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> register(@Valid @RequestBody RegisterDamageReportRequest requestDto, User user) {
		return ResponseEntity.ok().body(ApiResponse.of(damageReportService.register(requestDto, user.getId())));
	}

	@PutMapping
	public ResponseEntity<ApiResponse<Void>> update(@Valid @RequestBody UpdateDamageReportRequest requestDto, User user) {
		damageReportService.update(requestDto, user.getId());
		return ResponseEntity.ok().body(ApiResponse.of());
	}

	@GetMapping("/recent")
	public ResponseEntity<ApiResponse<List<DamageReportInfoResponse>>> findRecentReports(User user) {
		return ResponseEntity.ok().body(ApiResponse.of(damageReportService.findRecentReports(user.getId())));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<PagingResponse<DamageReportInfoResponse>>> findReports(
		@ModelAttribute PagingRequest pagingRequest,
		User user
	) {
		return ResponseEntity.ok().body(ApiResponse.of(damageReportService.findReports(user.getId(), pagingRequest)));
	}
}
