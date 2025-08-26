package co.unicauca.edu.co.contables.configuration.classesOfDocuments.domain.services;

import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.entity.DocumentClassEntity;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.mapper.DocumentClassDataMapper;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.repository.DocumentClassRepository;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.domain.mapper.DocumentClassDomainMapper;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.domain.models.DocumentClass;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.presentation.DTO.request.DocumentClassCreateReq;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.presentation.DTO.request.DocumentClassUpdateReq;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.documentClasses.DocumentClassesAlreadyExistsException;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.documentClasses.DocumentClassesNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentClassServiceImpl implements IDocumentClassService {

    private final DocumentClassRepository repository;
    private final DocumentClassDataMapper dataMapper;
    private final DocumentClassDomainMapper domainMapper;

    @Transactional
    public DocumentClass create(DocumentClassCreateReq request) {
        String standardizedName = standardizeName(request.getName());

        if (repository.existsByNameAndIdEnterprise(standardizedName, request.getIdEnterprise())) {
            throw new DocumentClassesAlreadyExistsException(standardizedName, request.getIdEnterprise());
        }
        DocumentClass domain = domainMapper.toDomain(request);
        domain.setName(standardizedName);
        DocumentClassEntity saved = repository.save(dataMapper.toEntity(domain));
        return dataMapper.toDomain(saved);
    }

    @Transactional
    public DocumentClass update(DocumentClassUpdateReq request) {
        DocumentClassEntity current = repository.findById(request.getId())
                .orElseThrow(DocumentClassesNotFoundException::new);

        String targetEnterprise = request.getIdEnterprise() != null ? request.getIdEnterprise() : current.getIdEnterprise();

        String standardizedName = standardizeName(request.getName());

        boolean nameChanged = standardizedName != null && !standardizedName.equals(current.getName());
        boolean enterpriseChanged = targetEnterprise != null && !targetEnterprise.equals(current.getIdEnterprise());

        if (nameChanged || enterpriseChanged) {
            if (repository.existsByNameAndIdEnterpriseAndIdNot(standardizedName, targetEnterprise, current.getId())) {
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
        return dataMapper.toDomain(repository.findByIdAndIdEnterprise(id, idEnterprise)
                .orElseThrow(DocumentClassesNotFoundException::new));
    }

    @Transactional(readOnly = true)
    public Page<DocumentClass> findAllByEnterprise(String idEnterprise, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByIdEnterprise(idEnterprise, pageable).map(dataMapper::toDomain);
    }

    @Transactional
    public void delete(Long id, String idEnterprise) {
        DocumentClassEntity entity = repository.findByIdAndIdEnterprise(id, idEnterprise).orElseThrow(DocumentClassesNotFoundException::new);
        repository.delete(entity);
    }

    private String standardizeName(String rawName) {
        if (rawName == null) return null;
        String s = rawName.trim().replaceAll("\\s+", " ").toLowerCase(new Locale("es", "ES"));
        if (s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase(new Locale("es", "ES")) + s.substring(1);
    }
}


