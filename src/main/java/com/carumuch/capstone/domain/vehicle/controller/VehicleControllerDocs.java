package com.carumuch.capstone.domain.vehicle.controller;

import com.carumuch.capstone.domain.vehicle.dto.VehicleRegistrationReqDto;
import com.carumuch.capstone.domain.vehicle.dto.VehicleUpdateReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Tag(name = "Vehicle")
@Validated
public interface VehicleControllerDocs {

    /* Create: 차량 등록 */
    @Operation(summary = "차량 등록 요청", description = "**성공 응답 데이터:** 차량의 `vehicle_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "차량 등록 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> register(VehicleRegistrationReqDto vehicleRegistrationReqDto);

    @Operation(summary = "차량 정보 수정 요청", description = "**성공 응답 데이터:** 차량의 `vehicle_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "차량 정보 수정 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 차량"),
            @ApiResponse(responseCode = "409", description = "차량 번호 중복 입니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> update(VehicleUpdateReqDto vehicleUpdateReqDto);


    /* Delete: 차량 삭제 */
    @Operation(summary = "차량 삭제 요청", description = "**성공 응답 데이터:** true ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "차량 삭제 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 차량"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> delete();

    /* Selete: 차량 상세 조회 */
    @Operation(summary = "차량 상세 조회 요청", description = "**성공 응답 데이터:** vehicle ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "차량 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 차량"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> findById(Long id);

    /* Selete: 내 차량 정보 조회 */
    @Operation(summary = "내 차량 정보 조회 요청", description = "**성공 응답 데이터:** vehicle ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "차량 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 차량"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> vehicleInfo();

    /**
     * info: 요구사항 변경 -> 차량 2대 이상에서 1대만 가지는것으로 변경 되었습니다.
     * Date: 2024.10.07
     */

    /* Select: 차량 목록 조회 */
//    @Operation(summary = "차량 목록 조회 요청", description = "**성공 응답 데이터:** vehicleList ")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "차량 목록 조회 성공"),
//            @ApiResponse(responseCode = "404", description = "등록된 차량이 없습니다."),
//            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
//            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
//    })
//    ResponseEntity<?> findAll();

    /* Update: 차량 정보 수정 */
//    @Operation(summary = "차량 정보 수정 요청", description = "**성공 응답 데이터:** 차량의 `vehicle_id` ")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "차량 정보 수정 완료"),
//            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
//            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
//            @ApiResponse(responseCode = "404", description = "존재 하지 않는 차량"),
//            @ApiResponse(responseCode = "409", description = "차량 번호 중복 입니다."),
//            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
//    })
//    ResponseEntity<?> update(VehicleUpdateReqDto vehicleUpdateReqDto, Long id);

    /* Delete: 차량 삭제 */
//    @Operation(summary = "차량 삭제 요청", description = "**성공 응답 데이터:** true ")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "차량 삭제 완료"),
//            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
//            @ApiResponse(responseCode = "404", description = "존재 하지 않는 차량"),
//            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
//            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
//    })
//    ResponseEntity<?> delete(Long id);
}
