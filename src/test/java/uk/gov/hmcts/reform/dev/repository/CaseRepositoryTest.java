package uk.gov.hmcts.reform.dev.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import uk.gov.hmcts.reform.dev.models.Case;
import uk.gov.hmcts.reform.dev.models.CaseStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CaseRepositoryTest {

    @Autowired
    private CaseRepository repository;

    private Case createTestCase() {

        Case caseEntity = new Case();

        caseEntity.setTitle("Review application");
        caseEntity.setDescription("Review documents");
        caseEntity.setStatus(CaseStatus.OPEN);
        caseEntity.setDueDate(LocalDateTime.now());

        return caseEntity;
    }

    @Test
    void shouldCreateCase() {

        Case saved = repository.save(createTestCase());

        assertNotNull(saved.getId());
        assertEquals("Review application", saved.getTitle());
    }

    @Test
    void shouldGetAllCases() {

        repository.save(createTestCase());

        Case second = createTestCase();
        second.setTitle("Prepare report");

        repository.save(second);

        List<Case> result = repository.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldGetCaseById() {

        Case saved = repository.save(createTestCase());

        Optional<Case> result =
            repository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Review application", result.get().getTitle());
    }

    @Test
    void shouldUpdateCase() {

        Case saved = repository.save(createTestCase());

        saved.setTitle("Updated application");
        saved.setStatus(CaseStatus.IN_PROGRESS);

        Case updated = repository.save(saved);

        assertEquals("Updated application", updated.getTitle());
        assertEquals(CaseStatus.IN_PROGRESS, updated.getStatus());
    }

    @Test
    void shouldDeleteCase() {

        Case saved = repository.save(createTestCase());

        repository.deleteById(saved.getId());

        assertFalse(repository.existsById(saved.getId()));
    }

    @Test
    void shouldReturnEmptyWhenCaseDoesNotExist() {

        Optional<Case> result = repository.findById(999L);

        assertTrue(result.isEmpty());
    }
}
