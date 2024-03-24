package com.vedha.webflux.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "EmployeeDTO", description = "Employee Data Transfer Object")
public class EmployeeDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    private String name;

    private String email;
}
