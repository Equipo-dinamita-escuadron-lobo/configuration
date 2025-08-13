package co.unicauca.edu.co.contables.configuration.commons.exceptions.costCenters;

import co.unicauca.edu.co.contables.configuration.commons.exceptions.BaseBusinessException;

/**
 * Excepción para cuando un centro de costo ya existe (por código o nombre) dentro de una empresa.
 */
public class CostCentersAlreadyExistsException extends BaseBusinessException {

    public CostCentersAlreadyExistsException() {
        super(CostCentersErrorCode.COST_CENTER_ALREADY_EXISTS);
    }

    public CostCentersAlreadyExistsException(Integer code, String idEnterprise) {
        super(
            CostCentersErrorCode.COST_CENTER_ALREADY_EXISTS,
            String.format("Ya existe un centro de costo con código %d en la empresa %s", code, idEnterprise)
        );
    }

    public CostCentersAlreadyExistsException(String name, String idEnterprise) {
        super(
            CostCentersErrorCode.COST_CENTER_ALREADY_EXISTS,
            String.format("Ya existe un centro de costo con nombre '%s' en la empresa %s", name, idEnterprise)
        );
    }

    public CostCentersAlreadyExistsException(Integer code, String name, String idEnterprise) {
        super(
            CostCentersErrorCode.COST_CENTER_ALREADY_EXISTS,
            String.format(
                "Ya existe un centro de costo con código %d o nombre '%s' en la empresa %s",
                code,
                name,
                idEnterprise
            )
        );
    }
}


