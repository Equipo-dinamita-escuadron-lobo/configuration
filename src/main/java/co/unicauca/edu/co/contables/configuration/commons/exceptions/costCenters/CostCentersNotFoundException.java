package co.unicauca.edu.co.contables.configuration.commons.exceptions.costCenters;

import co.unicauca.edu.co.contables.configuration.commons.exceptions.BaseBusinessException;

/**
 * Excepción para cuando un centro de costo no es encontrado.
 */
public class CostCentersNotFoundException extends BaseBusinessException {
    
    public CostCentersNotFoundException() {
        super(CostCentersErrorCode.COST_CENTER_NOT_FOUND);
    }
    
    public CostCentersNotFoundException(Long id) {
        super(CostCentersErrorCode.COST_CENTER_NOT_FOUND,
              String.format("El centro de costo con ID %d no se puede eliminar porque está asociado con ...", id));
    }
    
    public CostCentersNotFoundException(String name) {
        super(CostCentersErrorCode.COST_CENTER_NOT_FOUND,
              String.format("El centro de costo '%s' no se puede eliminar porque está asociado con ...", name));
    }
}
