package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Dice Game API Services")
                        .description("Dice Game related services for IT Academy Final Project")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Leandro Pereyra")
                                .url("https://github.com/leandropereyradev")
                        )
                );
    }
}