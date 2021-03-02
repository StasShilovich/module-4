package com.epam.esm.model.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CertificateDTO {

    private Long id;
    @Size(min = 2, max = 50)
    private String name;
    @Size(min = 2, max = 100)
    private String description;
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 5, fraction = 2)
    private BigDecimal price;
    @Min(1)
    private Integer duration;
    @NotBlank
    private String createDate;
    private String lastUpdateDate;
    private List<TagDTO> tags;
}
