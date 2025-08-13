package co.unicauca.edu.co.contables.configuration.costCenters.domain.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostCenter {
    private Long id;
    private String idEnterprise;
    private Integer code;
    private String name;
    private CostCenter parent;
    private List<CostCenter> children;
}


