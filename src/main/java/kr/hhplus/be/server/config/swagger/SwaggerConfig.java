package kr.hhplus.be.server.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(hhpApiInfo());
    }

    private Info hhpApiInfo() {
        return new Info()
                .title("항해 플러스 백엔드 2팀 김종완 E-Commerce API")
                .description("항해 플러스 백엔드 E-Commerce 시스템 API Document")
                .version("1.0.0");
    }
}
