package com.example.spring.test.member.service;

import com.example.spring.test.common.exception.CustomException;
import com.example.spring.test.common.exception.ErrorCode;
import com.example.spring.test.member.entity.Member;
import com.example.spring.test.member.entity.Role;
import com.example.spring.test.member.repository.MemberRepository;
import com.example.spring.test.member.request.LoginRequest;
import com.example.spring.test.member.request.SignUpRequest;
import com.example.spring.test.member.response.LoginResponse;
import com.example.spring.test.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        if (memberRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
        }

        Member member = Member.builder()
                .uuid(UUID.randomUUID().toString())
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.USER)
                .build();

        memberRepository.save(member);
    }

    public LoginResponse login (LoginRequest loginRequest) {

        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            member.getUuid(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }



        String token = jwtTokenProvider.createToken(member.getUuid(), member.getRole().name());

        return LoginResponse.builder()
                .accessToken(token)
                .name(member.getName())
                .build();

    }
}
