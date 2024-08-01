package com.carumuch.capstone.user.controller;

import com.carumuch.capstone.user.dto.UserInfoResDto;
import com.carumuch.capstone.user.dto.UserJoinReqDto;
import com.carumuch.capstone.user.dto.UserUpdatePasswordReqDto;
import com.carumuch.capstone.user.dto.UserUpdateReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Tag(name = "Users")
@Validated
public interface UserControllerDocs {

    /* Read: 회원 정보 조회 */
    @Operation(summary = "회원정보 조회 요청", description = "**성공 응답 데이터:** 사용자 `정보` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoResDto.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유요한 토큰이 아닙니다. 재 로그인이 필요합니다.")
    })
    ResponseEntity<?> info();

    /* Create: 회원 가입 */
    @Operation(summary = "회원가입 요청", description = "**성공 응답 데이터:** 사용자의 `user_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 가입 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "409", description = "아이디/이메일 중복")
    })
    ResponseEntity<?> join(UserJoinReqDto userJoinReqDto);


    /* 아이디 중복 확인 */
    @Operation(summary = "아이디 중복 확인", description = "**성공 응답 데이터:** true")
    @Parameter(name = "loginId", description = "검증할 아이디", example = "carumuch1234")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 아이디"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "409", description = "아이디 중복")
    })
    ResponseEntity<?> checkLoginId(
            @Pattern(regexp = "^[a-z0-9]{4,20}$",
                    message = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.") String loginId);


    /* 이메일 중복 확인 */
    @Operation(summary = "이메일 중복 확인", description = "**성공 응답 데이터:** true")
    @Parameter(name = "email", description = "검증할 이메일", example = "carumuch@gmail.com")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 이메일"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "409", description = "이메일 중복")
    })
    ResponseEntity<?> checkEmail(
            @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
                    message = "이메일 형식에 맞지 않습니다.") String email);

    @Operation(summary = "회원탈퇴 요청", description = "**성공 응답 데이터:**  `쿠키` 초기화")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 탈퇴 완료"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다 혹은 서버 측 소셜 로그인 토큰이 만료 되었습니다. 재 로그인이 필요합니다."),
    })
    ResponseEntity<?> delete(HttpServletRequest request);

    @Operation(summary = "회원 수정 요청", description = "**성공 응답 데이터:**  사용자의 `user_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 수정 완료"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다 혹은 서버 측 소셜 로그인 토큰이 만료 되었습니다. 재 로그인이 필요합니다."),
            @ApiResponse(responseCode = "409", description = "이메일 중복")
    })
    ResponseEntity<?> update(UserUpdateReqDto userUpdateReqDto);

    @Operation(summary = "비밀번호 수정 요청", description = "**성공 응답 데이터:**  사용자의 `user_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "비밀번호 수정 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다 혹은 서버 측 소셜 로그인 토큰이 만료 되었습니다. 재 로그인이 필요합니다.")
    })
    ResponseEntity<?> updatePassword(UserUpdatePasswordReqDto userUpdatePasswordReqDto);
}
