package com.tenpo.calculator.config;

import com.tenpo.calculator.util.Constants;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title(Constants.SWAGGER_INFO_TITLE)
                        .description(Constants.SWAGGER_INFO_DESCRIPTION)
                        .version(Constants.SWAGGER_INFO_VERSION));
    }
}
