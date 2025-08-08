package co.unicauca.edu.co.contables.configuration.costCenters.exceptions.costCenters;

import co.unicauca.edu.co.contables.configuration.costCenters.exceptions.BaseBusinessException;
import co.unicauca.edu.co.contables.configuration.costCenters.exceptions.ErrorCode;

public class CostsCentersException extends BaseBusinessException{

    public CostsCentersException(ErrorCode errorCode) {
        super(ErrorCode.COST_CENTER_NOT_FOUND);
        //TODO Auto-generated constructor stub
    }

}
