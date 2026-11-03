package com.example.spring.test.security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_SCHEME = "bearerAuth";

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("로그인으로 받은 accessToken을 입력하세요. Bearer 접두사는 자동으로 붙습니다.");

        return new OpenAPI()
                .info(new Info()
                        .title("Spring API")
                        .description("JWT 인증이 적용된 API 문서입니다.")
                        .version("v1"))
                // Gateway Swagger UI에서 Try it out 시 /{service-id} 경로로 호출되도록 설정
                .servers(List.of(new Server()
                        .url("/" + applicationName)
                        .description("API Gateway")))
                .components(new Components().addSecuritySchemes(BEARER_SCHEME, bearerScheme))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME));
    }

    @Bean
    public OpenApiCustomizer publicApiCustomizer() {
        return openApi -> {
            if (openApi.getPaths() == null) {
                return;
            }

            openApi.getPaths().forEach((path, pathItem) -> {
                if (path.startsWith("/auth")) {
                    pathItem.readOperations().forEach(operation -> operation.setSecurity(List.of()));
                    return;
                }

                if (path.startsWith("/product") && pathItem.getGet() != null) {
                    pathItem.getGet().setSecurity(List.of());
                }
            });
        };
    }
}
