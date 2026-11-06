package uk.gov.hmcts.reform.dev.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.dev.models.Case;
import jakarta.validation.Valid;
import uk.gov.hmcts.reform.dev.services.CaseService;
import java.util.List;


@RestController
@RequestMapping("/cases/")
@Tag(name = "Cases", description = "Case management operations")
public class CaseController {

    private final CaseService service;

    public CaseController(CaseService service) {
        this.service = service;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Case> create(
        @Valid @RequestBody Case caseEntity) {

        Case created = service.create(caseEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Case>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Case> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Case> update(
        @PathVariable Long id,
        @Valid @RequestBody Case caseEntity) {

        Case updated = service.update(id, caseEntity);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
