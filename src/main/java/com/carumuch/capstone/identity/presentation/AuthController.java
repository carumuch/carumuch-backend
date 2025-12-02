package com.carumuch.capstone.identity.presentation;

import static com.carumuch.capstone.identity.infrastructure.jwt.JwtConstants.*;
import static org.springframework.http.HttpHeaders.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carumuch.capstone.identity.application.AccountRecoveryService;
import com.carumuch.capstone.identity.application.AuthService;
import com.carumuch.capstone.identity.presentation.dto.request.auth.AuthenticateUserRequest;
import com.carumuch.capstone.identity.presentation.dto.request.auth.ResetPasswordRequest;
import com.carumuch.capstone.identity.presentation.dto.request.auth.RetrieveLoginIdRequest;
import com.carumuch.capstone.identity.presentation.dto.request.auth.RetrievePasswordRequest;
import com.carumuch.capstone.identity.presentation.dto.request.auth.VerifyPasswordCodeRequest;
import com.carumuch.capstone.identity.presentation.dto.response.auth.AuthenticateUserResponse;
import com.carumuch.capstone.identity.presentation.dto.response.auth.ReissueTokenResponse;
import com.carumuch.capstone.identity.presentation.dto.response.auth.VerifyPasswordCodeResponse;
import com.carumuch.capstone.common.infrastructure.web.CookieUtil;
import com.carumuch.capstone.common.presentation.dto.ApiResponse;
import com.carumuch.capstone.identity.domain.user.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	@Value("${jwt.refresh-token-valid-days}")
	private Long REFRESH_TOKEN_VALID_DAYS;

	@Value("${cookie.name}")
	private String REFRESH_TOKEN_COOKIE_NAME;

    private final AuthService authService;
	private final AccountRecoveryService accountRecoveryService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> authenticate(@RequestBody @Valid AuthenticateUserRequest authenticateUserRequest) {
        AuthenticateUserResponse authenticateUserResponse = authService.authenticate(authenticateUserRequest);

        ResponseCookie responseCookie = CookieUtil.of(
			REFRESH_TOKEN_COOKIE_NAME,
			authenticateUserResponse.refreshToken(),
			REFRESH_TOKEN_VALID_DAYS * 24 * 60 * 60
		);

        return ResponseEntity.ok()
                .header(AUTHORIZATION, BEARER_PREFIX + authenticateUserResponse.accessToken())
                .header(SET_COOKIE, responseCookie.toString())
                .body(ApiResponse.of());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> invalidate(User user) {

        authService.invalidate(user.getLoginId());
        ResponseCookie responseExpiredCookie = CookieUtil.ofExpired(REFRESH_TOKEN_COOKIE_NAME);

        return ResponseEntity.ok()
                .header(SET_COOKIE, responseExpiredCookie.toString())
                .body(ApiResponse.of());
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<Void>> reissueToken(HttpServletRequest request) {

		String refreshToken = CookieUtil.findCookieByName(request, REFRESH_TOKEN_COOKIE_NAME).getValue();
		ReissueTokenResponse reissueTokenResponse = authService.reissueToken(refreshToken);

        return ResponseEntity.ok()
                .header(AUTHORIZATION, BEARER_PREFIX + reissueTokenResponse.accessToken())
                .body(ApiResponse.of());
    }

	@PostMapping("/id/find")
	public ResponseEntity<ApiResponse<Void>> retrieveLoginId(@RequestBody @Valid RetrieveLoginIdRequest retrieveLoginIdRequest) {
		accountRecoveryService.retrieveLoginId(retrieveLoginIdRequest);
		return ResponseEntity.ok().body(ApiResponse.of());
	}

	@PostMapping("/password/find")
	public ResponseEntity<ApiResponse<Void>> retrievePassword(@RequestBody @Valid RetrievePasswordRequest retrievePasswordRequest) {
		accountRecoveryService.retrievePassword(retrievePasswordRequest);
		return ResponseEntity.ok().body(ApiResponse.of());
	}

	@PostMapping("/password/verify")
	public ResponseEntity<ApiResponse<VerifyPasswordCodeResponse>> verifyCode(@RequestBody @Valid VerifyPasswordCodeRequest verifyPasswordCodeRequest) {
		return ResponseEntity.ok().body(ApiResponse.of(accountRecoveryService.verifyCode(verifyPasswordCodeRequest)));
	}

	@PostMapping("/password/reset")
	public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
		accountRecoveryService.resetPassword(resetPasswordRequest);
		return ResponseEntity.ok().body(ApiResponse.of());
	}
}
