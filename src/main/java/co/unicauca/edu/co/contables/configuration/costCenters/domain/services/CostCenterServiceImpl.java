package co.unicauca.edu.co.contables.configuration.costCenters.domain.services;

import co.unicauca.edu.co.contables.configuration.commons.exceptions.costCenters.CostCentersAlreadyExistsException;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.costCenters.CostCentersNotFoundException;
import co.unicauca.edu.co.contables.configuration.costCenters.dataAccess.entity.CostCenterEntity;
import co.unicauca.edu.co.contables.configuration.costCenters.dataAccess.mapper.CostCenterDataMapper;
import co.unicauca.edu.co.contables.configuration.costCenters.dataAccess.repository.CostCenterRepository;
import co.unicauca.edu.co.contables.configuration.costCenters.domain.mapper.CostCenterDomainMapper;
import co.unicauca.edu.co.contables.configuration.costCenters.domain.models.CostCenter;
import co.unicauca.edu.co.contables.configuration.costCenters.presentation.DTO.request.CostCenterCreateReq;
import co.unicauca.edu.co.contables.configuration.costCenters.presentation.DTO.request.CostCenterUpdateReq;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CostCenterServiceImpl implements ICostCenterService {

	private final CostCenterRepository repository;
	private final CostCenterDataMapper dataMapper;
	private final CostCenterDomainMapper domainMapper;

	@Transactional
	public CostCenter create(CostCenterCreateReq request) {
		// Validación de unicidad por código y nombre dentro de la empresa
		if (repository.existsByCodeAndIdEnterprise(request.getCode(), request.getIdEnterprise())) {
			throw new CostCentersAlreadyExistsException(request.getCode(), request.getIdEnterprise());
		}
		// Estandarizar nombre: primera letra mayúscula, resto minúsculas, colapsar espacios
		String standardizedName = standardizeName(request.getName());
		request.setName(standardizedName);

		// Validación de nombre exacto (tras estandarización)
		if (repository.existsByNameAndIdEnterprise(standardizedName, request.getIdEnterprise())) {
			throw new CostCentersAlreadyExistsException(request.getName(), request.getIdEnterprise(), true);
		}

		CostCenter costCenter = domainMapper.toDomain(request);
		CostCenterEntity entity = dataMapper.toEntity(costCenter);
		if (request.getParentId() != null) {
			CostCenterEntity parent = repository.findByIdAndIdEnterprise(request.getParentId(), request.getIdEnterprise())
					.orElseThrow(CostCentersNotFoundException::new);
			entity.setParent(parent);
		}

		CostCenterEntity saved = repository.save(entity);
		return dataMapper.toDomain(saved);
	}

	@Transactional
	public CostCenter update(CostCenterUpdateReq request) {
		CostCenterEntity current = repository.findById(request.getId())
				.orElseThrow(CostCentersNotFoundException::new);

		// Estandarizar nombre antes de validar
		String standardizedName = standardizeName(request.getName());
		request.setName(standardizedName);

		// Si cambian code o name, validar que no exista otro con esos datos en la misma empresa
        boolean codeChanged = request.getCode() != null && !request.getCode().equals(current.getCode());
		boolean nameChanged = request.getName() != null && !request.getName().equals(current.getName());
		boolean enterpriseChanged = request.getIdEnterprise() != null && !request.getIdEnterprise().equals(current.getIdEnterprise());

		String targetEnterprise = enterpriseChanged ? request.getIdEnterprise() : current.getIdEnterprise();

        if (codeChanged || enterpriseChanged) {
            boolean existsCode = repository.existsByCodeAndIdEnterprise(request.getCode(), targetEnterprise);
			if (existsCode) {
				throw new CostCentersAlreadyExistsException(request.getCode(), targetEnterprise);
			}
		}
		if (nameChanged || enterpriseChanged) {
			boolean existsName = repository.existsByNameAndIdEnterpriseAndIdNot(request.getName(), targetEnterprise, current.getId());
			if (existsName) {
				throw new CostCentersAlreadyExistsException(request.getName(), targetEnterprise, true);
			}
		}

		current.setIdEnterprise(request.getIdEnterprise());
		current.setCode(request.getCode());
		current.setName(request.getName());
		if (request.getParentId() != null) {
			CostCenterEntity parent = repository.findByIdAndIdEnterprise(request.getParentId(), targetEnterprise)
					.orElseThrow(CostCentersNotFoundException::new);
			current.setParent(parent);
		} else {
			current.setParent(null);
		}

		return dataMapper.toDomain(repository.save(current));
	}

    @Transactional(readOnly = true)
    public Page<CostCenter> findAllByEnterprise(String idEnterprise, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return repository.findAllByIdEnterprise(idEnterprise, pageable)
				.map(dataMapper::toDomain);
	}

    @Transactional(readOnly = true)
    public CostCenter findById(Long id, String idEnterprise) {
		return dataMapper.toDomain(repository.findByIdAndIdEnterprise(id, idEnterprise)
				.orElseThrow(CostCentersNotFoundException::new));
	}

	@Transactional
	public void delete(Long id, String idEnterprise) {
		CostCenterEntity entity = repository.findByIdAndIdEnterprise(id, idEnterprise)
				.orElseThrow(CostCentersNotFoundException::new);
		repository.delete(entity);
	}

	private String standardizeName(String input) {
		if (input == null) return null;
		String s = input.trim().replaceAll("\\s+", " ").toLowerCase(new Locale("es", "ES"));
		if (s.isEmpty()) return s;
		return s.substring(0, 1).toUpperCase(new Locale("es", "ES")) + s.substring(1);
	}
}


