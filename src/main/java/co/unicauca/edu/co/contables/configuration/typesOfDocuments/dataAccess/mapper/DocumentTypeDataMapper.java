package co.unicauca.edu.co.contables.configuration.typesOfDocuments.dataAccess.mapper;

import co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.entity.DocumentClassEntity;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.dataAccess.entity.DocumentTypeEntity;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.domain.models.DocumentType;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DocumentTypeDataMapper {

    @Mappings({
            @Mapping(target = "documentClass", expression = "java(mapDocumentClass(domain.getDocumentClassId()))"),
            @Mapping(target = "tenantId", ignore = true)
    })
    DocumentTypeEntity toEntity(DocumentType domain);

    @Mappings({
            @Mapping(target = "documentClassId", expression = "java(entity.getDocumentClass() != null ? entity.getDocumentClass().getId() : null)")
    })
    DocumentType toDomain(DocumentTypeEntity entity);

    default DocumentClassEntity mapDocumentClass(Long id) {
        if (id == null) return null;
        DocumentClassEntity ref = new DocumentClassEntity();
        ref.setId(id);
        return ref;
    }
}


