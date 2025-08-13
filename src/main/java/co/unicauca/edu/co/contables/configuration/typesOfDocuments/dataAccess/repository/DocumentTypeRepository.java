package co.unicauca.edu.co.contables.configuration.typesOfDocuments.dataAccess.repository;

import co.unicauca.edu.co.contables.configuration.typesOfDocuments.dataAccess.entity.DocumentTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DocumentTypeRepository extends JpaRepository<DocumentTypeEntity, Long> {
    boolean existsByPrefixAndIdEnterprise(String prefix, String idEnterprise);
    boolean existsByPrefixAndIdEnterpriseAndIdNot(String prefix, String idEnterprise, Long id);
    boolean existsByNameAndIdEnterprise(String name, String idEnterprise);
    boolean existsByNameAndIdEnterpriseAndIdNot(String name, String idEnterprise, Long id);
    Page<DocumentTypeEntity> findAllByIdEnterprise(String idEnterprise, Pageable pageable);

    Optional<DocumentTypeEntity> findByIdAndIdEnterprise(Long id, String idEnterprise);
}


