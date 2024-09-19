package com.interview_master.common.interceptor;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.common.token.AuthTokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements WebGraphQlInterceptor {

    private final AuthTokenGenerator tokenGenerator;

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String token = request.getHeaders().getFirst("Access-Token");

        if (token != null && !token.isEmpty()) {
            try {
                Long userId = tokenGenerator.extractUserId(token);
                log.info("========== intercept... userId = {}", userId);
                request.configureExecutionInput((executionInput, builder) ->
                        builder.graphQLContext(context -> context.put("userId", userId)).build());
            } catch (Exception e) {
                return Mono.error(new ApiException(ErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND));
            }
        }

        return chain.next(request);
    }
}