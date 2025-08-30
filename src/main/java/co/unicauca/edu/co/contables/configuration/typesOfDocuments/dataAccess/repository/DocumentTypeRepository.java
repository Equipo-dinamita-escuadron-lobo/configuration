package co.unicauca.edu.co.contables.configuration.typesOfDocuments.dataAccess.repository;

import co.unicauca.edu.co.contables.configuration.typesOfDocuments.dataAccess.entity.DocumentTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DocumentTypeRepository extends JpaRepository<DocumentTypeEntity, Long> {

    // Métodos con soft delete
    boolean existsByPrefixAndIdEnterpriseAndIsDeletedFalse(String prefix, String idEnterprise);

    boolean existsByPrefixAndIdEnterpriseAndIdNotAndIsDeletedFalse(String prefix, String idEnterprise, Long id);

    boolean existsByNameAndIdEnterpriseAndIsDeletedFalse(String name, String idEnterprise);

    boolean existsByNameAndIdEnterpriseAndIdNotAndIsDeletedFalse(String name, String idEnterprise, Long id);

    Page<DocumentTypeEntity> findAllByIdEnterpriseAndIsDeletedFalse(String idEnterprise, Pageable pageable);

    Optional<DocumentTypeEntity> findByIdAndIdEnterpriseAndIsDeletedFalse(Long id, String idEnterprise);
    
    Page<DocumentTypeEntity> findAllByModuleAndIdEnterpriseAndIsDeletedFalse(String module, String idEnterprise, Pageable pageable);

    // Método para verificar si una clase de documento está siendo usada por tipos activos
    boolean existsByDocumentClassIdAndIsDeletedFalse(Long documentClassId);
}


