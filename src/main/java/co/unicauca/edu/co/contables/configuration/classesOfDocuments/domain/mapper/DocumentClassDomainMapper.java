package co.unicauca.edu.co.contables.configuration.classesOfDocuments.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.unicauca.edu.co.contables.configuration.classesOfDocuments.domain.models.DocumentClass;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.presentation.DTO.request.DocumentClassCreateReq;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.presentation.DTO.request.DocumentClassUpdateReq;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.presentation.DTO.response.DocumentClassRes;

@Mapper(componentModel = "spring")
public interface DocumentClassDomainMapper {
    @Mapping(target = "id", ignore = true)
    DocumentClass toDomain(DocumentClassCreateReq req);

    DocumentClass toDomain(DocumentClassUpdateReq req);

    DocumentClassRes toRes(DocumentClass domain);
}


