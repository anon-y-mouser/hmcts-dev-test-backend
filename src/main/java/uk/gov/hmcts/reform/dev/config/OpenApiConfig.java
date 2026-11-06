package uk.gov.hmcts.reform.dev.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI caseManagementOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                      .title("Case Management API")
                      .description("REST API for managing cases")
                      .version("1.0.0"));
    }
}
