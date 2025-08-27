package co.unicauca.edu.co.contables.configuration.typesOfDocuments.domain.services;

import co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.entity.DocumentClassEntity;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.repository.DocumentClassRepository;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.documentClasses.DocumentClassesNotFoundException;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.documentTypes.DocumentTypesAlreadyExistsException;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.documentTypes.DocumentTypesNotFoundException;
import co.unicauca.edu.co.contables.configuration.commons.utils.StringStandardizationUtils;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.dataAccess.entity.DocumentTypeEntity;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.dataAccess.mapper.DocumentTypeDataMapper;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.dataAccess.repository.DocumentTypeRepository;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.domain.mapper.DocumentTypeDomainMapper;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.domain.models.DocumentType;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.presentation.DTO.request.DocumentTypeCreateReq;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.presentation.DTO.request.DocumentTypeUpdateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DocumentTypeServiceImpl implements IDocumentTypeService {

    private static final Set<String> ALLOWED_MODULES = new HashSet<>(Arrays.asList(
            "Inventario promedio ponderado",
            "Inventario PEPS",
            "Comercial",
            "Tesorería",
            "Cartera",
            "Contable comercial",
            "Contable cartera",
            "Estados financieros"
    ));

    private final DocumentTypeRepository repository;
    private final DocumentTypeDataMapper dataMapper;
    private final DocumentTypeDomainMapper domainMapper;
    private final DocumentClassRepository documentClassRepository;

    @Transactional
    public DocumentType create(DocumentTypeCreateReq request) {
        // Validación de módulo permitido (insensible a mayúsculas/minúsculas)
        if (!isModuleAllowed(request.getModule())) {
            throw new IllegalArgumentException("Modulo invalido");
        }

        // Estandarización de nombre y prefijo
        String standardizedName = StringStandardizationUtils.standardizeName(request.getName());
        String standardizedPrefix = StringStandardizationUtils.standardizePrefix(request.getPrefix());

        // Unicidad por empresa: prefijo y nombre (solo registros no eliminados)
        if (repository.existsByPrefixAndIdEnterpriseAndIsDeletedFalse(standardizedPrefix, request.getIdEnterprise())) {
            throw new DocumentTypesAlreadyExistsException("prefijo", standardizedPrefix, request.getIdEnterprise());
        }
        if (repository.existsByNameAndIdEnterpriseAndIsDeletedFalse(standardizedName, request.getIdEnterprise())) {
            throw new DocumentTypesAlreadyExistsException("nombre", standardizedName, request.getIdEnterprise());
        }

        // Validar que la clase exista y esté activa
        DocumentClassEntity docClass = documentClassRepository.findByIdAndIdEnterpriseAndIsDeletedFalse(request.getDocumentClassId(), request.getIdEnterprise())
                .orElseThrow(DocumentClassesNotFoundException::new);

        DocumentType domain = domainMapper.toDomain(request);
        domain.setName(standardizedName);
        domain.setPrefix(standardizedPrefix);
        domain.setModule(StringStandardizationUtils.standardizeName(request.getModule()));
        DocumentTypeEntity toSave = dataMapper.toEntity(domain);
        toSave.setDocumentClass(docClass);

        DocumentTypeEntity saved = repository.save(toSave);
        return dataMapper.toDomain(saved);
    }

    @Transactional
    public DocumentType update(DocumentTypeUpdateReq request) {
        // Validación de módulo permitido (insensible a mayúsculas/minúsculas)
        if (!isModuleAllowed(request.getModule())) {
            throw new IllegalArgumentException("Módulo inválido");
        }

        DocumentTypeEntity current = repository.findByIdAndIdEnterpriseAndIsDeletedFalse(request.getId(), request.getIdEnterprise())
                .orElseThrow(DocumentTypesNotFoundException::new);

        String targetEnterprise = request.getIdEnterprise() != null ? request.getIdEnterprise() : current.getIdEnterprise();
        String standardizedName = StringStandardizationUtils.standardizeName(request.getName());
        String standardizedPrefix = StringStandardizationUtils.standardizePrefix(request.getPrefix());

        boolean prefixChanged = standardizedPrefix != null && !standardizedPrefix.equals(current.getPrefix());
        boolean nameChanged = standardizedName != null && !standardizedName.equals(current.getName());
        boolean enterpriseChanged = targetEnterprise != null && !targetEnterprise.equals(current.getIdEnterprise());

        // Validar unicidad del prefijo si cambió (solo entre registros no eliminados)
        if (prefixChanged || enterpriseChanged) {
            if (repository.existsByPrefixAndIdEnterpriseAndIdNotAndIsDeletedFalse(standardizedPrefix, targetEnterprise, current.getId())) {
                throw new DocumentTypesAlreadyExistsException("prefijo", standardizedPrefix, targetEnterprise);
            }
        }
        // Validar unicidad del nombre si cambió (solo entre registros no eliminados)
        if (nameChanged || enterpriseChanged) {
            if (repository.existsByNameAndIdEnterpriseAndIdNotAndIsDeletedFalse(standardizedName, targetEnterprise, current.getId())) {
                throw new DocumentTypesAlreadyExistsException("nombre", standardizedName, targetEnterprise);
            }
        }

        // Validar clase de documento activa
        DocumentClassEntity docClass = documentClassRepository.findByIdAndIdEnterpriseAndIsDeletedFalse(request.getDocumentClassId(), targetEnterprise)
                .orElseThrow(DocumentClassesNotFoundException::new);

        current.setIdEnterprise(targetEnterprise);
        current.setPrefix(standardizedPrefix);
        current.setName(standardizedName);
        current.setDocumentClass(docClass);
        current.setModule(StringStandardizationUtils.standardizeName(request.getModule()));

        DocumentTypeEntity saved = repository.save(current);
        return dataMapper.toDomain(saved);
    }

    @Transactional(readOnly = true)
    public DocumentType findById(Long id, String idEnterprise) {
        return dataMapper.toDomain(repository.findByIdAndIdEnterpriseAndIsDeletedFalse(id, idEnterprise).orElseThrow(DocumentTypesNotFoundException::new));
    }

    @Transactional(readOnly = true)
    public Page<DocumentType> findAllByEnterprise(String idEnterprise, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByIdEnterpriseAndIsDeletedFalse(idEnterprise, pageable).map(dataMapper::toDomain);
    }

    @Transactional(readOnly = true)
    public Page<DocumentType> findAllByModuleAndEnterprise(String module, String idEnterprise, int page, int size) {
        // Validar que el módulo esté permitido
        if (!isModuleAllowed(module)) {
            throw new IllegalArgumentException("Modulo invalido: " + module);
        }
        
        // Estandarizar el módulo para la búsqueda
        String standardizedModule = StringStandardizationUtils.standardizeName(module);
        
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByModuleAndIdEnterpriseAndIsDeletedFalse(standardizedModule, idEnterprise, pageable)
                .map(dataMapper::toDomain);
    }

    @Transactional
    public DocumentType changeState(Long id, String idEnterprise, Boolean status) {
        DocumentTypeEntity current = repository.findByIdAndIdEnterpriseAndIsDeletedFalse(id, idEnterprise)
                .orElseThrow(DocumentTypesNotFoundException::new);

        current.setStatus(status);
        DocumentTypeEntity saved = repository.save(current);
        return dataMapper.toDomain(saved);
    }

    @Transactional
    public DocumentType softDelete(Long id, String idEnterprise) {
        DocumentTypeEntity current = repository.findByIdAndIdEnterpriseAndIsDeletedFalse(id, idEnterprise)
                .orElseThrow(DocumentTypesNotFoundException::new);

        current.setIsDeleted(true);
        DocumentTypeEntity saved = repository.save(current);
        return dataMapper.toDomain(saved);
    }

    /**
     * Valida si un módulo está permitido, sin distinguir entre mayúsculas y minúsculas
     * @param module el módulo a validar
     * @return true si el módulo está permitido, false en caso contrario
     */
    private boolean isModuleAllowed(String module) {
        if (module == null) return false;
        
        String normalizedModule = module.trim().toLowerCase(new Locale("es", "ES"));
        
        return ALLOWED_MODULES.stream()
                .map(allowed -> allowed.toLowerCase(new Locale("es", "ES")))
                .anyMatch(allowed -> allowed.equals(normalizedModule));
    }

}
