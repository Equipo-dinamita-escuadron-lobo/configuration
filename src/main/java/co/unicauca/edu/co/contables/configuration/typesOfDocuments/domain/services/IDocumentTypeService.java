package co.unicauca.edu.co.contables.configuration.typesOfDocuments.domain.services;

import org.springframework.data.domain.Page;

import co.unicauca.edu.co.contables.configuration.typesOfDocuments.domain.models.DocumentType;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.presentation.DTO.request.DocumentTypeCreateReq;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.presentation.DTO.request.DocumentTypeUpdateReq;

public interface IDocumentTypeService {

	DocumentType create(DocumentTypeCreateReq request);

	DocumentType update(DocumentTypeUpdateReq request);

	DocumentType findById(Long id, String idEnterprise);

	Page<DocumentType> findAllByEnterprise(String idEnterprise, int page, int size);
	
	Page<DocumentType> findAllByModuleAndEnterprise(String module, String idEnterprise, int page, int size);

	void delete(Long id, String idEnterprise);
}
