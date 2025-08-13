package co.unicauca.edu.co.contables.configuration.commons.exceptions.documentClasses;

import co.unicauca.edu.co.contables.configuration.commons.exceptions.BaseBusinessException;

public class DocumentClassesAlreadyExistsException extends BaseBusinessException {

    public DocumentClassesAlreadyExistsException() {
        super(DocumentClassesErrorCode.DOCUMENT_CLASS_ALREADY_EXISTS);
    }

    public DocumentClassesAlreadyExistsException(String name, String idEnterprise) {
        super(
            DocumentClassesErrorCode.DOCUMENT_CLASS_ALREADY_EXISTS,
            String.format("Ya existe una clase de documento con nombre '%s' en la empresa %s", name, idEnterprise)
        );
    }
}


