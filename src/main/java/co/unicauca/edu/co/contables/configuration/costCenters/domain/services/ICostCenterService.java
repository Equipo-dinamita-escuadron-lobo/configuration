package co.unicauca.edu.co.contables.configuration.costCenters.domain.services;

import co.unicauca.edu.co.contables.configuration.costCenters.domain.models.CostCenter;
import co.unicauca.edu.co.contables.configuration.costCenters.presentation.DTO.request.CostCenterCreateReq;
import co.unicauca.edu.co.contables.configuration.costCenters.presentation.DTO.request.CostCenterUpdateReq;
import org.springframework.data.domain.Page;

public interface ICostCenterService {

	CostCenter create(CostCenterCreateReq request);

	CostCenter update(CostCenterUpdateReq request);

	Page<CostCenter> findAllByEnterprise(String idEnterprise, int page, int size);

	CostCenter findById(Long id, String idEnterprise);

	void delete(Long id, String idEnterprise);
}

