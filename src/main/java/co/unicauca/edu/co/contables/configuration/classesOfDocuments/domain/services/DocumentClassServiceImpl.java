package co.unicauca.edu.co.contables.configuration.classesOfDocuments.domain.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.entity.DocumentClassEntity;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.mapper.DocumentClassDataMapper;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.repository.DocumentClassRepository;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.domain.mapper.DocumentClassDomainMapper;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.domain.models.DocumentClass;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.presentation.DTO.request.DocumentClassCreateReq;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.presentation.DTO.request.DocumentClassUpdateReq;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.documentClasses.DocumentClassesAlreadyExistsException;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.documentClasses.DocumentClassesNotFoundException;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.documentClasses.DocumentClassInUseException;
import co.unicauca.edu.co.contables.configuration.commons.utils.StringStandardizationUtils;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.dataAccess.repository.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentClassServiceImpl implements IDocumentClassService {

    private final DocumentClassRepository repository;
    private final DocumentClassDataMapper dataMapper;
    private final DocumentClassDomainMapper domainMapper;
    private final DocumentTypeRepository documentTypeRepository;

    @Transactional
    public DocumentClass create(DocumentClassCreateReq request) {
        String standardizedName = StringStandardizationUtils.standardizeName(request.getName());

        // Validar unicidad del nombre por empresa (solo registros no eliminados)
        if (repository.existsByNameAndIdEnterpriseAndIsDeletedFalse(standardizedName, request.getIdEnterprise())) {
            throw new DocumentClassesAlreadyExistsException(standardizedName, request.getIdEnterprise());
        }
        DocumentClass domain = domainMapper.toDomain(request);
        domain.setName(standardizedName);
        DocumentClassEntity saved = repository.save(dataMapper.toEntity(domain));
        return dataMapper.toDomain(saved);
    }

    @Transactional
    public DocumentClass update(DocumentClassUpdateReq request) {
        DocumentClassEntity current = repository.findByIdAndIdEnterpriseAndIsDeletedFalse(request.getId(), request.getIdEnterprise())
                .orElseThrow(DocumentClassesNotFoundException::new);

        String targetEnterprise = request.getIdEnterprise() != null ? request.getIdEnterprise() : current.getIdEnterprise();

        String standardizedName = StringStandardizationUtils.standardizeName(request.getName());

        boolean nameChanged = standardizedName != null && !standardizedName.equals(current.getName());
        boolean enterpriseChanged = targetEnterprise != null && !targetEnterprise.equals(current.getIdEnterprise());

        // Validar unicidad del nombre si cambió (solo entre registros no eliminados)
        if (nameChanged || enterpriseChanged) {
            if (repository.existsByNameAndIdEnterpriseAndIdNotAndIsDeletedFalse(standardizedName, targetEnterprise, current.getId())) {
                throw new DocumentClassesAlreadyExistsException(standardizedName, targetEnterprise);
            }
        }

        current.setName(standardizedName);
        current.setIdEnterprise(targetEnterprise);

        DocumentClassEntity saved = repository.save(current);
        return dataMapper.toDomain(saved);
    }

    @Transactional(readOnly = true)
    public DocumentClass findById(Long id, String idEnterprise) {
        return dataMapper.toDomain(repository.findByIdAndIdEnterpriseAndIsDeletedFalse(id, idEnterprise)
                .orElseThrow(DocumentClassesNotFoundException::new));
    }

    @Transactional(readOnly = true)
    public Page<DocumentClass> findAllByEnterprise(String idEnterprise, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByIdEnterpriseAndIsDeletedFalse(idEnterprise, pageable).map(dataMapper::toDomain);
    }

    @Transactional(readOnly = true)
    public Page<DocumentClass> findAllByEnterprise(String idEnterprise, int page, int size, String sortField, String sortOrder) {
        Sort sort = "desc".equalsIgnoreCase(sortOrder) ? 
            Sort.by(sortField).descending() : 
            Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAllByIdEnterpriseAndIsDeletedFalse(idEnterprise, pageable).map(dataMapper::toDomain);
    }

    public Page<DocumentClass> findAllByEnterpriseAndStatus(String idEnterprise, Boolean status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByIdEnterpriseAndStatusAndIsDeletedFalse(idEnterprise, status, pageable).map(dataMapper::toDomain);
    }

    @Transactional
    public DocumentClass changeState(Long id, String idEnterprise, Boolean status) {
        DocumentClassEntity current = repository.findByIdAndIdEnterpriseAndIsDeletedFalse(id, idEnterprise)
                .orElseThrow(DocumentClassesNotFoundException::new);

        current.setStatus(status);
        DocumentClassEntity saved = repository.save(current);
        return dataMapper.toDomain(saved);
    }

    @Transactional
    public DocumentClass softDelete(Long id, String idEnterprise) {
        DocumentClassEntity current = repository.findByIdAndIdEnterpriseAndIsDeletedFalse(id, idEnterprise)
                .orElseThrow(DocumentClassesNotFoundException::new);

        // Validar que la clase de documento no esté siendo utilizada por tipos de documentos activos
        if (documentTypeRepository.existsByDocumentClassIdAndIsDeletedFalse(id)) {
            throw new DocumentClassInUseException(current.getName());
        }

        current.setIsDeleted(true);
        DocumentClassEntity saved = repository.save(current);
        return dataMapper.toDomain(saved);
    }

}


