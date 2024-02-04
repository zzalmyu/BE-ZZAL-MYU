package com.prgrms.be.domain.user.presentation.controller;

import com.prgrms.be.domain.user.application.UserService;
import com.prgrms.be.domain.user.presentation.dto.req.UserSignUpRequest;
import com.prgrms.be.domain.user.presentation.dto.res.UserSignUpResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<UserSignUpResponse> signUp(
        @RequestBody @Valid UserSignUpRequest request) {
        UserSignUpResponse response = userService.signUp(request);
        return ResponseEntity.ok(response);
    }
}
