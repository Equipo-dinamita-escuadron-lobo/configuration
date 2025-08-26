package co.unicauca.edu.co.contables.configuration.costCenters.dataAccess.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import co.unicauca.edu.co.contables.configuration.costCenters.dataAccess.entity.CostCenterEntity;


public interface CostCenterRepository extends JpaRepository<CostCenterEntity, Long> {

    CostCenterEntity findByCodeAndIdEnterprise(String code, String idEnterprise);

    boolean existsByCodeAndIdEnterprise(String code, String idEnterprise);

    boolean existsByNameAndIdEnterprise(String name, String idEnterprise);

    boolean existsByNameAndIdEnterpriseAndIdNot(String name, String idEnterprise, Long id);

    Page<CostCenterEntity> findAllByIdEnterprise(String idEnterprise, Pageable pageable);

    Optional<CostCenterEntity> findByIdAndIdEnterprise(Long id, String idEnterprise);
}


