package co.unicauca.edu.co.contables.configuration.classesOfDocuments.presentation.controller;

import co.unicauca.edu.co.contables.configuration.classesOfDocuments.domain.models.DocumentClass;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.domain.services.IDocumentClassService;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.domain.mapper.DocumentClassDomainMapper;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.presentation.DTO.request.DocumentClassCreateReq;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.presentation.DTO.request.DocumentClassUpdateReq;
import co.unicauca.edu.co.contables.configuration.classesOfDocuments.presentation.DTO.response.DocumentClassRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/config/document-classes")
@RequiredArgsConstructor
public class DocumentClassController {

    private final IDocumentClassService service;
    private final DocumentClassDomainMapper mapper;

    @PostMapping("/create")
    public ResponseEntity<DocumentClassRes> create(@Valid @RequestBody DocumentClassCreateReq request) {
        DocumentClass created = service.create(request);
        return ResponseEntity.ok(mapper.toRes(created));
    }

    @PutMapping("/update")
    public ResponseEntity<DocumentClassRes> update(@Valid @RequestBody DocumentClassUpdateReq request) {
        DocumentClass updated = service.update(request);
        return ResponseEntity.ok(mapper.toRes(updated));
    }

    @GetMapping("/findById/{id}/{enterpriseId}")
    public ResponseEntity<DocumentClassRes> getById(@PathVariable Long id, @PathVariable String enterpriseId) {
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

    @GetMapping("/findAllActive/{enterpriseId}")
    public ResponseEntity<?> listActive(@PathVariable("enterpriseId") String enterpriseId) {
        return ResponseEntity.ok(service.findAllByEnterpriseAndStatus(enterpriseId, true, 0, 1000)
                .map(mapper::toRes));
    }

    @PatchMapping("/changeState/{id}/{enterpriseId}")
    public ResponseEntity<DocumentClassRes> changeState(
            @PathVariable Long id,
            @PathVariable String enterpriseId,
            @RequestParam Boolean status) {
        DocumentClass updated = service.changeState(id, enterpriseId, status);
        return ResponseEntity.ok(mapper.toRes(updated));
    }

    @DeleteMapping("/delete/{id}/{enterpriseId}")
    public ResponseEntity<DocumentClassRes> softDelete(
            @PathVariable Long id,
            @PathVariable String enterpriseId) {
        DocumentClass deleted = service.softDelete(id, enterpriseId);
        return ResponseEntity.ok(mapper.toRes(deleted));
    }
}


