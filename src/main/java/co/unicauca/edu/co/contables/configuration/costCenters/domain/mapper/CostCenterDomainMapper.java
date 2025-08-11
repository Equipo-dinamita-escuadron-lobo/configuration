package co.unicauca.edu.co.contables.configuration.costCenters.domain.mapper;

import co.unicauca.edu.co.contables.configuration.costCenters.domain.models.CostCenter;
import co.unicauca.edu.co.contables.configuration.costCenters.presentation.DTO.request.CostCenterCreateReq;
import co.unicauca.edu.co.contables.configuration.costCenters.presentation.DTO.request.CostCenterUpdateReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CostCenterDomainMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "children", ignore = true),
            @Mapping(target = "parent", expression = "java(request.getParentId() != null ? CostCenter.builder().id(request.getParentId()).build() : null)")
    })
    CostCenter toDomain(CostCenterCreateReq request);

    @Mappings({
            @Mapping(target = "children", ignore = true),
            @Mapping(target = "parent", expression = "java(request.getParentId() != null ? CostCenter.builder().id(request.getParentId()).build() : null)")
    })
    CostCenter toDomain(CostCenterUpdateReq request);
}


