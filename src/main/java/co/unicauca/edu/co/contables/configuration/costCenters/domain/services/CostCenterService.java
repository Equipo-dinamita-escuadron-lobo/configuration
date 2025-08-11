package co.unicauca.edu.co.contables.configuration.costCenters.domain.services;

import co.unicauca.edu.co.contables.configuration.commons.exceptions.costCenters.CostCentersNotFoundException;
import co.unicauca.edu.co.contables.configuration.costCenters.dataAccess.entity.CostCenterEntity;
import co.unicauca.edu.co.contables.configuration.costCenters.dataAccess.mapper.CostCenterDataMapper;
import co.unicauca.edu.co.contables.configuration.costCenters.dataAccess.repository.CostCenterRepository;
import co.unicauca.edu.co.contables.configuration.costCenters.domain.mapper.CostCenterDomainMapper;
import co.unicauca.edu.co.contables.configuration.costCenters.domain.models.CostCenter;
import co.unicauca.edu.co.contables.configuration.costCenters.presentation.DTO.request.CostCenterCreateReq;
import co.unicauca.edu.co.contables.configuration.costCenters.presentation.DTO.request.CostCenterUpdateReq;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CostCenterService {

    private final CostCenterRepository repository;
    private final CostCenterDataMapper dataMapper;
    private final CostCenterDomainMapper domainMapper;

    @Transactional
    public CostCenter create(CostCenterCreateReq request) {
        CostCenter costCenter = domainMapper.toDomain(request);
        CostCenterEntity entity = dataMapper.toEntity(costCenter);
        if (request.getParentId() != null) {
            entity.setParent(repository.findById(request.getParentId()).orElse(null));
        }

        CostCenterEntity saved = repository.save(entity);
        return dataMapper.toDomain(saved);
    }

    @Transactional
    public CostCenter update(CostCenterUpdateReq request) {
        CostCenterEntity current = repository.findById(request.getId())
                .orElseThrow(CostCentersNotFoundException::new);

        current.setIdEnterprise(request.getIdEnterprise());
        current.setCode(request.getCode());
        current.setName(request.getName());
        if (request.getParentId() != null) {
            current.setParent(repository.findById(request.getParentId()).orElse(null));
        } else {
            current.setParent(null);
        }

        return dataMapper.toDomain(repository.save(current));
    }

    public List<CostCenter> findAllByEnterprise(String idEnterprise) {
        return repository.findAllByIdEnterprise(idEnterprise).stream()
                .map(dataMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Page<CostCenter> findAllByEnterprise(String idEnterprise, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByIdEnterprise(idEnterprise, pageable)
                .map(dataMapper::toDomain);
    }

    public CostCenter findById(Long id) {
        return dataMapper.toDomain(repository.findById(id)
                .orElseThrow(CostCentersNotFoundException::new));
    }

    @Transactional
    public void delete(Long id) {
        CostCenterEntity entity = repository.findById(id).orElseThrow(CostCentersNotFoundException::new);
        repository.delete(entity);
    }
}


