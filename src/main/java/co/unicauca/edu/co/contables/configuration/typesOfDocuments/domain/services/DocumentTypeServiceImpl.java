package co.unicauca.edu.co.contables.configuration.typesOfDocuments.domain.services;

import co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.entity.DocumentClassEntity;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.dataAccess.repository.DocumentClassRepository;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.documentClasses.DocumentClassesNotFoundException;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.documentTypes.DocumentTypesAlreadyExistsException;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.documentTypes.DocumentTypesNotFoundException;
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
        String standardizedName = standardizeName(request.getName());
        String standardizedPrefix = standardizePrefix(request.getPrefix());

        // Unicidad por empresa: prefijo y nombre
        if (repository.existsByPrefixAndIdEnterprise(standardizedPrefix, request.getIdEnterprise())) {
            throw new DocumentTypesAlreadyExistsException("prefijo", standardizedPrefix, request.getIdEnterprise());
        }
        if (repository.existsByNameAndIdEnterprise(standardizedName, request.getIdEnterprise())) {
            throw new DocumentTypesAlreadyExistsException("nombre", standardizedName, request.getIdEnterprise());
        }

        // Validar que la clase exista
        DocumentClassEntity docClass = documentClassRepository.findById(request.getDocumentClassId())
                .orElseThrow(DocumentClassesNotFoundException::new);

        DocumentType domain = domainMapper.toDomain(request);
        domain.setName(standardizedName);
        domain.setPrefix(standardizedPrefix);
        domain.setModule(standardizeName(request.getModule()));
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

        DocumentTypeEntity current = repository.findById(request.getId())
                .orElseThrow(DocumentTypesNotFoundException::new);

        String targetEnterprise = request.getIdEnterprise() != null ? request.getIdEnterprise() : current.getIdEnterprise();
        String standardizedName = standardizeName(request.getName());
        String standardizedPrefix = standardizePrefix(request.getPrefix());

        boolean prefixChanged = standardizedPrefix != null && !standardizedPrefix.equals(current.getPrefix());
        boolean nameChanged = standardizedName != null && !standardizedName.equals(current.getName());
        boolean enterpriseChanged = targetEnterprise != null && !targetEnterprise.equals(current.getIdEnterprise());

        if (prefixChanged || enterpriseChanged) {
            if (repository.existsByPrefixAndIdEnterpriseAndIdNot(standardizedPrefix, targetEnterprise, current.getId())) {
                throw new DocumentTypesAlreadyExistsException("prefijo", standardizedPrefix, targetEnterprise);
            }
        }
        if (nameChanged || enterpriseChanged) {
            if (repository.existsByNameAndIdEnterpriseAndIdNot(standardizedName, targetEnterprise, current.getId())) {
                throw new DocumentTypesAlreadyExistsException("nombre", standardizedName, targetEnterprise);
            }
        }

        // Validar clase de documento
        DocumentClassEntity docClass = documentClassRepository.findById(request.getDocumentClassId())
                .orElseThrow(DocumentClassesNotFoundException::new);

        current.setIdEnterprise(targetEnterprise);
        current.setPrefix(standardizedPrefix);
        current.setName(standardizedName);
        current.setDocumentClass(docClass);
        current.setModule(standardizeName(request.getModule()));

        DocumentTypeEntity saved = repository.save(current);
        return dataMapper.toDomain(saved);
    }

    @Transactional(readOnly = true)
    public DocumentType findById(Long id, String idEnterprise) {
        return dataMapper.toDomain(repository.findByIdAndIdEnterprise(id, idEnterprise).orElseThrow(DocumentTypesNotFoundException::new));
    }

    @Transactional(readOnly = true)
    public Page<DocumentType> findAllByEnterprise(String idEnterprise, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByIdEnterprise(idEnterprise, pageable).map(dataMapper::toDomain);
    }

    @Transactional(readOnly = true)
    public Page<DocumentType> findAllByModuleAndEnterprise(String module, String idEnterprise, int page, int size) {
        // Validar que el módulo esté permitido
        if (!isModuleAllowed(module)) {
            throw new IllegalArgumentException("Modulo invalido: " + module);
        }
        
        // Estandarizar el módulo para la búsqueda
        String standardizedModule = standardizeName(module);
        
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByModuleAndIdEnterprise(standardizedModule, idEnterprise, pageable)
                .map(dataMapper::toDomain);
    }

    @Transactional
    public void delete(Long id, String idEnterprise) {
        DocumentTypeEntity entity = repository.findByIdAndIdEnterprise(id, idEnterprise).orElseThrow(DocumentTypesNotFoundException::new);
        repository.delete(entity);
    }

    private String standardizeName(String input) {
        if (input == null) return null;
        String s = input.trim().replaceAll("\\s+", " ").toLowerCase(new Locale("es", "ES"));
        if (s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase(new Locale("es", "ES")) + s.substring(1);
    }

    private String standardizePrefix(String input) {
        if (input == null) return null;
        return input.trim().toUpperCase(Locale.ROOT);
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
