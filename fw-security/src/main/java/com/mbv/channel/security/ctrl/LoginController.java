package com.fw.channel.security.ctrl;

import com.fw.channel.security.dto.LoginResponse;
import com.fw.channel.security.dto.TokenRequest;
import com.fw.channel.security.service.LoginService;
import com.fw.core.base.BaseController;
import com.fw.core.base.ResponseData;
import com.fw.model.dto.exception.BusinessException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequestMapping("/v1.0/auth")
public class LoginController extends BaseController {
	@Autowired
	private LoginService loginService;


	@ApiOperation("API tạo token khi đăng nhập")
	@GetMapping(value = "/generate-token")
	public ResponseEntity<ResponseData<Object>> authenticate(
			@RequestHeader(name = "Authorization", required = false) @ApiParam(example = "Basic cXV5bnE6ZW5hb0AxMjM=") String authorization)
			throws BusinessException {

		return ResponseEntity.ok()
				.body(new ResponseData<>().success(loginService.generateToken(authorization)));
	}

	@ApiOperation("API tạo token khi đăng nhập")
	@PostMapping(value = "/refresh-token")
	public ResponseEntity<ResponseData<Object>> refreshToken(
			@RequestHeader(name = "Authorization", required = false) @ApiParam(example = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJBbVE2eW14S3RLY0c0c3dQYW5hRzlxNmZrMXk4THJsaWtaSGNDZzk4emNjIn0") String authorization,
			@RequestBody TokenRequest refreshToken)
			throws BusinessException {

		return ResponseEntity.ok()
				.body(new ResponseData<>().success(loginService.refresh( authorization, refreshToken.getRefreshToken(), getUsername())));
	}

	//	@PreAuthorize("!isAnonymous()")
	@GetMapping(value = "/user-info")
	public ResponseEntity<ResponseData<Object>> getUserInfo(@RequestHeader(name = "Authorization", required = false)String authorization) throws BusinessException {
		return ResponseEntity.ok().body(
				new ResponseData<>().success(loginService.getUserInfo(authorization)));
	}

	@ApiOperation("API hủy token khi đăng xuất")
	@PostMapping(value = "/revoke-token")
	public ResponseEntity<LoginResponse> revoke(@RequestBody TokenRequest refreshToken, @RequestHeader(name = "Authorization", required = false) String authorization) {
		loginService.revoke(refreshToken.getRefreshToken(),authorization);
		return ResponseEntity.ok().build();
	}
}
