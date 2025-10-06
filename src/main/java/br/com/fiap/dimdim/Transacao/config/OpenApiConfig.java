package br.com.fiap.dimdim.Transacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Transações Bancárias DimDim")
                        .version("v1.0")
                        .description("API para simulação de operações bancárias, como criação de contas e transferências de valores.")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org"))
                        .contact(new Contact()
                                .name("Seu Nome ou Nome do Time")
                                .email("seu.email@fiap.com.br")
                                .url("https://www.fiap.com.br"))
                );
    }
}
