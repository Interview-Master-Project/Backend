package com.interview_master.config;

import com.interview_master.common.resolver.CurrentUserArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.data.method.annotation.support.AnnotatedControllerConfigurer;

@Configuration
public class GraphQLConfig extends AnnotatedControllerConfigurer {

    @Bean
    public AnnotatedControllerConfigurer annotatedControllerConfigurer() {
        AnnotatedControllerConfigurer annotatedControllerConfigurer = new AnnotatedControllerConfigurer();
        annotatedControllerConfigurer.addCustomArgumentResolver(new CurrentUserArgumentResolver());
        return annotatedControllerConfigurer;
    }
}
