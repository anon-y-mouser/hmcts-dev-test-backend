package uk.gov.hmcts.reform.dev.services;


import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.exceptions.CaseNotFoundException;
import uk.gov.hmcts.reform.dev.models.Case;
import uk.gov.hmcts.reform.dev.repository.CaseRepository;

import java.util.List;

@Service
public class CaseService {

    private final CaseRepository repository;

    public CaseService(CaseRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public Case create(Case caseEntity) {
        return repository.save(caseEntity);
    }

    // READ ALL
    public List<Case> getAll() {
        return repository.findAll();
    }

    // READ BY ID
    public Case getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new CaseNotFoundException(id));
    }

    // UPDATE
    public Case update(Long id, Case updatedCase) {

        Case existing = getById(id);

        existing.setTitle(updatedCase.getTitle());
        existing.setDescription(updatedCase.getDescription());
        existing.setStatus(updatedCase.getStatus());
        existing.setDueDate(updatedCase.getDueDate());

        return repository.save(existing);
    }

    // DELETE
    public void delete(Long id) {

        Case caseEntity = repository.findById(id)
            .orElseThrow(() -> new CaseNotFoundException(id));

        repository.delete(caseEntity);
    }
}
