package com.interview_master.login;

import com.interview_master.common.token.AuthTokenGenerator;
import com.interview_master.common.token.AuthTokens;
import com.interview_master.domain.user.User;
import com.interview_master.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final UserRepository userRepository;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final AuthTokenGenerator authTokenGenerator;
    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long userId = findOrCreateUser(oAuthInfoResponse);
        return authTokenGenerator.generate(userId);
    }

    private Long findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
        return userRepository.findByEmail(oAuthInfoResponse.getEmail())
            .map(User::getId)
            .orElseGet(() -> newUser(oAuthInfoResponse));
    }

    private Long newUser(OAuthInfoResponse oAuthInfoResponse) {
        User newUser = new User(
                oAuthInfoResponse.getNickname(),
                oAuthInfoResponse.getEmail(),
                oAuthInfoResponse.getOAuthProvider());
        return userRepository.save(newUser).getId();
    }
}