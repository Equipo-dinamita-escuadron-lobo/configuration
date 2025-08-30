package co.unicauca.edu.co.contables.configuration.classesOfDocuments.domain.services;

import org.springframework.data.domain.Page;

import co.unicauca.edu.co.contables.configuration.classesOfDocuments.domain.models.DocumentClass;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.presentation.DTO.request.DocumentClassCreateReq;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.presentation.DTO.request.DocumentClassUpdateReq;

public interface IDocumentClassService {

	DocumentClass create(DocumentClassCreateReq request);

	DocumentClass update(DocumentClassUpdateReq request);

	DocumentClass findById(Long id, String idEnterprise);

	Page<DocumentClass> findAllByEnterprise(String idEnterprise, int page, int size);

	Page<DocumentClass> findAllByEnterprise(String idEnterprise, int page, int size, String sortField, String sortOrder);

	Page<DocumentClass> findAllByEnterpriseAndStatus(String idEnterprise, Boolean status, int page, int size);

	DocumentClass changeState(Long id, String idEnterprise, Boolean status);

	DocumentClass softDelete(Long id, String idEnterprise);
}
