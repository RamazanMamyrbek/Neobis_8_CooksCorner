package com.neobis.cookscorner.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEditDto {
    @Schema(example = "testUser")
    private String name;
    @Schema(example = "testUser is only for testing!")
    private String description;
    private MultipartFile photo;
}
