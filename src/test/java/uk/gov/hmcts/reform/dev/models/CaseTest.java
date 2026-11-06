package uk.gov.hmcts.reform.dev.models;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CaseTest {

    private final Validator validator =
        Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldCreateCase() {

        Case caseEntity = new Case();

        caseEntity.setTitle("Review application");
        caseEntity.setDescription("Review submitted documents");
        caseEntity.setStatus(CaseStatus.OPEN);
        caseEntity.setDueDate(
            LocalDateTime.of(2026, 11, 15, 17, 0)
        );

        assertEquals("Review application", caseEntity.getTitle());
        assertEquals("Review submitted documents",
                     caseEntity.getDescription());
        assertEquals(CaseStatus.OPEN, caseEntity.getStatus());
        assertNotNull(caseEntity.getDueDate());
    }

    @Test
    void shouldAllowOptionalDescription() {

        Case caseEntity = new Case();

        caseEntity.setTitle("Prepare report");
        caseEntity.setDescription(null);
        caseEntity.setStatus(CaseStatus.OPEN);
        caseEntity.setDueDate(LocalDateTime.now());

        assertTrue(validator.validate(caseEntity).isEmpty());
    }

    @Test
    void shouldRejectBlankTitle() {

        Case caseEntity = new Case();

        caseEntity.setTitle("");
        caseEntity.setStatus(CaseStatus.OPEN);
        caseEntity.setDueDate(LocalDateTime.now());

        assertFalse(validator.validate(caseEntity).isEmpty());
    }

    @Test
    void shouldRejectMissingStatus() {

        Case caseEntity = new Case();

        caseEntity.setTitle("Review application");
        caseEntity.setDueDate(LocalDateTime.now());

        assertFalse(validator.validate(caseEntity).isEmpty());
    }

    @Test
    void shouldRejectMissingDueDateTime() {

        Case caseEntity = new Case();

        caseEntity.setTitle("Review application");
        caseEntity.setStatus(CaseStatus.OPEN);

        assertFalse(validator.validate(caseEntity).isEmpty());
    }
}
