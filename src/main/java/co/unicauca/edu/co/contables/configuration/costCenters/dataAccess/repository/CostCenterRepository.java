package co.unicauca.edu.co.contables.configuration.costCenters.dataAccess.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import co.unicauca.edu.co.contables.configuration.costCenters.dataAccess.entity.CostCenterEntity;


public interface CostCenterRepository extends JpaRepository<CostCenterEntity, Long> {

    CostCenterEntity findByCodeAndIdEnterprise(Integer code, String idEnterprise);

    boolean existsByCodeAndIdEnterprise(Integer code, String idEnterprise);

    boolean existsByNameAndIdEnterprise(String name, String idEnterprise);

    boolean existsByNameAndIdEnterpriseAndIdNot(String name, String idEnterprise, Long id);

    Page<CostCenterEntity> findAllByIdEnterprise(String idEnterprise, Pageable pageable);
}


