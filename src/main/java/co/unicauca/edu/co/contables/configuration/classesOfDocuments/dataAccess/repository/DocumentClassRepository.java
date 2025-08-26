package co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


import co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.entity.DocumentClassEntity;

public interface DocumentClassRepository extends JpaRepository<DocumentClassEntity, Long> {
    boolean existsByNameAndIdEnterprise(String name, String idEnterprise);
    boolean existsByNameAndIdEnterpriseAndIdNot(String name, String idEnterprise, Long id);
    Page<DocumentClassEntity> findAllByIdEnterprise(String idEnterprise, Pageable pageable);

    Optional<DocumentClassEntity> findByIdAndIdEnterprise(Long id, String idEnterprise);
}


