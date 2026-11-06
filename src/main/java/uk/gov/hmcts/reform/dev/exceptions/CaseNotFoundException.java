package uk.gov.hmcts.reform.dev.exceptions;

public class CaseNotFoundException extends RuntimeException {

    public CaseNotFoundException(Long id) {
        super("Case not found with id: " + id);
    }
}
