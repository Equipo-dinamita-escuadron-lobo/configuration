package co.unicauca.edu.co.contables.configuration.costCenters.presentation.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostCenterCreateReq {

    @NotBlank
    private String idEnterprise;

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private Long parentId; // opcional
}


