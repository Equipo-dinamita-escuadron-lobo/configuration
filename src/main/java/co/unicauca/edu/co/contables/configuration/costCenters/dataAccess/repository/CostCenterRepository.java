package co.unicauca.edu.co.contables.configuration.costCenters.dataAccess.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import co.unicauca.edu.co.contables.configuration.costCenters.dataAccess.entity.CostCenterEntity;


public interface CostCenterRepository extends JpaRepository<CostCenterEntity, Long> {

    // Métodos con soft delete
    boolean existsByCodeAndIdEnterpriseAndIsDeletedFalse(String code, String idEnterprise);

    boolean existsByNameAndIdEnterpriseAndIsDeletedFalse(String name, String idEnterprise);

    boolean existsByNameAndIdEnterpriseAndIdNotAndIsDeletedFalse(String name, String idEnterprise, Long id);

    Page<CostCenterEntity> findAllByIdEnterpriseAndIsDeletedFalse(String idEnterprise, Pageable pageable);

    Page<CostCenterEntity> findAllByIdEnterpriseAndStatusAndIsDeletedFalse(String idEnterprise, Boolean status, Pageable pageable);

    Optional<CostCenterEntity> findByIdAndIdEnterpriseAndIsDeletedFalse(Long id, String idEnterprise);
}


