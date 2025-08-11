package co.unicauca.edu.co.contables.configuration.costCenters.presentation.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostCenterUpdateReq {

    @NotNull
    private Long id;

    @NotBlank
    private String idEnterprise;

    @NotNull
    private Integer code;

    @NotBlank
    private String name;

    private Long parentId; // opcional
}


