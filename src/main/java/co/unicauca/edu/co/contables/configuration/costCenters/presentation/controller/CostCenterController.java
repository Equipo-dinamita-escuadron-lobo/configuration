package co.unicauca.edu.co.contables.configuration.costCenters.presentation.controller;

import co.unicauca.edu.co.contables.configuration.costCenters.domain.models.CostCenter;
import co.unicauca.edu.co.contables.configuration.costCenters.domain.services.CostCenterService;
import co.unicauca.edu.co.contables.configuration.costCenters.presentation.DTO.request.CostCenterCreateReq;
import co.unicauca.edu.co.contables.configuration.costCenters.presentation.DTO.request.CostCenterUpdateReq;
import co.unicauca.edu.co.contables.configuration.costCenters.presentation.DTO.response.CostCenterRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/config/cost-centers")
@RequiredArgsConstructor
public class CostCenterController {

    private final CostCenterService service;

    @PostMapping("/create")
    public ResponseEntity<CostCenterRes> create(@Valid @RequestBody CostCenterCreateReq request) {
        CostCenter created = service.create(request);
        return ResponseEntity.ok(toRes(created));
    }

    @PutMapping("/update")
    public ResponseEntity<CostCenterRes> update(@Valid @RequestBody CostCenterUpdateReq request) {
        CostCenter updated = service.update(request);
        return ResponseEntity.ok(toRes(updated));
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<CostCenterRes> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toRes(service.findById(id)));
    }

    @GetMapping("/findAll/{enterpriseId}")
    public ResponseEntity<?> list(
            @PathVariable String enterpriseId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            Page<CostCenter> pageResult = service.findAllByEnterprise(enterpriseId, page, size);
            Page<CostCenterRes> mapped = pageResult.map(this::toRes);
            return ResponseEntity.ok(mapped);
        }
        List<CostCenterRes> list = service.findAllByEnterprise(enterpriseId).stream()
                .map(this::toRes)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private CostCenterRes toRes(CostCenter domain) {
        return CostCenterRes.builder()
                .id(domain.getId())
                .idEnterprise(domain.getIdEnterprise())
                .code(domain.getCode())
                .name(domain.getName())
                .parentId(domain.getParent() != null ? domain.getParent().getId() : null)
                .build();
    }
}


