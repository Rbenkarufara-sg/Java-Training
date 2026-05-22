package com.s_giken.training.webapp.model.entity;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Charge {

    @Nullable
    private Long chargeId;

    @NotBlank
    @Size(min=1, max=127)
    private String name;

    @NotBlank
    @Size(min=1, max=9)
    private Long amount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Nullable
    private LocalDate endDate;

    @Nullable
    private Timestamp createdAt;

    @Nullable
    private Timestamp modifiedAt;
}
