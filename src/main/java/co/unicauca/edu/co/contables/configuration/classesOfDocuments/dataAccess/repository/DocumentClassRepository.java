package co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.entity.DocumentClassEntity;

public interface DocumentClassRepository extends JpaRepository<DocumentClassEntity, Long> {
    boolean existsByNameAndIdEnterprise(String name, String idEnterprise);
    boolean existsByNameAndIdEnterpriseAndIdNot(String name, String idEnterprise, Long id);
    List<DocumentClassEntity> findAllByIdEnterprise(String idEnterprise);
    Page<DocumentClassEntity> findAllByIdEnterprise(String idEnterprise, Pageable pageable);
}


