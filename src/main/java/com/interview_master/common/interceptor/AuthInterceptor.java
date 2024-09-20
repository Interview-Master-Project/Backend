package com.interview_master.common.interceptor;

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
        String token = request.getHeaders().getFirst("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // Remove "Bearer " prefix
            try {
                Long userId = tokenGenerator.extractUserId(token);
                log.info("User authenticated: userId = {}", userId);
                request.configureExecutionInput((executionInput, builder) ->
                        builder.graphQLContext(context -> context.put("userId", userId)).build());
            } catch (Exception e) {
                log.error("Failed to authenticate token", e);
                request.configureExecutionInput((executionInput, builder) ->
                        builder.graphQLContext(context -> context.put("authError", "INVALID_TOKEN")).build());
            }
        } else {
            log.warn("No valid Authorization header found");
            request.configureExecutionInput((executionInput, builder) ->
                    builder.graphQLContext(context -> context.put("authError", "NO_TOKEN")).build());
        }

        return chain.next(request);
    }
}