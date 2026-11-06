
package uk.gov.hmcts.reform.dev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;

import jakarta.validation.Valid;
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
    @Operation(summary = "Create a case",
        description = "Creates a new case")
    @ApiResponses({
        @ApiResponse(responseCode = "201",
            description = "Case successfully created"),
        @ApiResponse(responseCode = "400",
            description = "Invalid case details",
            content = @Content)
    })
    public ResponseEntity<Case> create(
        @Valid @RequestBody Case caseEntity) {

        Case created = service.create(caseEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all cases",
        description = "Returns all registered cases")
    @ApiResponse(responseCode = "200",
        description = "Cases successfully retrieved")
    public ResponseEntity<List<Case>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get case by ID",
        description = "Returns a case using its unique ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200",
            description = "Case found"),
        @ApiResponse(responseCode = "404",
            description = "Case not found",
            content = @Content)
    })
    public ResponseEntity<Case> getById(
        @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a case",
        description = "Updates an existing case")
    @ApiResponses({
        @ApiResponse(responseCode = "200",
            description = "Case successfully updated"),
        @ApiResponse(responseCode = "400",
            description = "Invalid case details",
            content = @Content),
        @ApiResponse(responseCode = "404",
            description = "Case not found",
            content = @Content)
    })
    public ResponseEntity<Case> update(
        @PathVariable Long id,
        @Valid @RequestBody Case caseEntity) {

        return ResponseEntity.ok(service.update(id, caseEntity));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a case",
        description = "Deletes a case using its unique ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204",
            description = "Case successfully deleted"),
        @ApiResponse(responseCode = "404",
            description = "Case not found",
            content = @Content)
    })
    public ResponseEntity<Void> delete(
        @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
