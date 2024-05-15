package com.neobis.cookscorner.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDto {
    @Schema(example = "testUser")
    private String name;
    @Schema(example = "user@mail.com")
    private String email;
    @Schema(example = "Password1234.")
    private String password;
}
