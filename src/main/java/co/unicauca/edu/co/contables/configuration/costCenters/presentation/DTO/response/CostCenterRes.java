package co.unicauca.edu.co.contables.configuration.costCenters.presentation.DTO.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostCenterRes {
    private Long id;
    private String idEnterprise;
    private Integer code;
    private String name;
    private Long parentId;
}


