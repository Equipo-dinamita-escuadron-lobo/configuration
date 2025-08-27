package co.unicauca.edu.co.contables.configuration.typesOfDocuments.presentation.controller;

import co.unicauca.edu.co.contables.configuration.typesOfDocuments.domain.models.DocumentType;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.domain.services.IDocumentTypeService;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.domain.mapper.DocumentTypeDomainMapper;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.presentation.DTO.request.DocumentTypeCreateReq;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.presentation.DTO.request.DocumentTypeUpdateReq;
import co.unicauca.edu.co.contables.configuration.typesOfDocuments.presentation.DTO.response.DocumentTypeRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config/document-types")
@RequiredArgsConstructor
public class DocumentTypeController {

    private final IDocumentTypeService service;
    private final DocumentTypeDomainMapper mapper;

    @PostMapping("/create")
    public ResponseEntity<DocumentTypeRes> create(@Valid @RequestBody DocumentTypeCreateReq request) {
        DocumentType created = service.create(request);
        return ResponseEntity.ok(mapper.toRes(created));
    }

    @PutMapping("/update")
    public ResponseEntity<DocumentTypeRes> update(@Valid @RequestBody DocumentTypeUpdateReq request) {
        DocumentType updated = service.update(request);
        return ResponseEntity.ok(mapper.toRes(updated));
    }

    @GetMapping("/findById/{id}/{enterpriseId}")
    public ResponseEntity<DocumentTypeRes> getById(@PathVariable Long id, @PathVariable String enterpriseId) {
        return ResponseEntity.ok(mapper.toRes(service.findById(id, enterpriseId)));
    }

    @GetMapping("/findAll/{enterpriseId}")
    public ResponseEntity<?> list(
            @PathVariable("enterpriseId") String enterpriseId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.findAllByEnterprise(enterpriseId, page, size)
                .map(mapper::toRes));
    }

    @GetMapping("/findAllByModule/{enterpriseId}")
    public ResponseEntity<?> listByModule(
            @PathVariable("enterpriseId") String enterpriseId,
            @RequestParam String module,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.findAllByModuleAndEnterprise(module, enterpriseId, page, size)
                .map(mapper::toRes));
    }

    @PatchMapping("/changeState/{id}/{enterpriseId}")
    public ResponseEntity<DocumentTypeRes> changeState(
            @PathVariable Long id,
            @PathVariable String enterpriseId,
            @RequestParam Boolean status) {
        DocumentType updated = service.changeState(id, enterpriseId, status);
        return ResponseEntity.ok(mapper.toRes(updated));
    }

    @DeleteMapping("/delete/{id}/{enterpriseId}")
    public ResponseEntity<DocumentTypeRes> softDelete(
            @PathVariable Long id,
            @PathVariable String enterpriseId) {
        DocumentType deleted = service.softDelete(id, enterpriseId);
        return ResponseEntity.ok(mapper.toRes(deleted));
    }
}