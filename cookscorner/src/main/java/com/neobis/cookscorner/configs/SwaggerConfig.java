package com.neobis.cookscorner.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Neobis Project - 9 - Cooks Corner ",
                description = "CooksCorner - это инновационное предложение, спроектированное для удобного и вдохновляющего опыта в мире кулинарии. Предлагая разнообразные категории, включая обширный список рецептов, CooksCorner создает удобную платформу для кулинарных энтузиастов. Погружайтесь в атмосферу кулинарных шедевров с захватывающими фотографиями, изучайте подробные описания рецептов, а также управляйте своим кулинарным опытом, сохраняя, лайкая и даже создавая собственные блюда. \n" +
                        "CooksCorner -  путь к беспроблемным и вдохновляющим кулинарным приключениям.",
                version = "1.0.0",
                contact = @Contact(
                        name = "Ramazan Mamyrbek",
                        email = "rama.mamirbek@gmail.com"
                )
        )
)
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {
}
