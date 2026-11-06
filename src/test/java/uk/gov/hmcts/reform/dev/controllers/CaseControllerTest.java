package uk.gov.hmcts.reform.dev.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.dev.exceptions.CaseNotFoundException;
import uk.gov.hmcts.reform.dev.models.Case;
import uk.gov.hmcts.reform.dev.models.CaseStatus;
import uk.gov.hmcts.reform.dev.services.CaseService;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(CaseController.class)
@Import({JacksonAutoConfiguration.class})
class CaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .findAndRegisterModules();

    @MockitoBean
    private CaseService service;

    private Case createTestCase() {

        Case caseEntity = new Case();

        caseEntity.setId(1L);
        caseEntity.setTitle("Review application");
        caseEntity.setDescription("Review documents");
        caseEntity.setStatus(CaseStatus.OPEN);
        caseEntity.setDueDate(
            LocalDateTime.of(2026, 11, 15, 17, 0)
        );

        return caseEntity;
    }

    @Test
    void shouldCreateCase() throws Exception {

        Case caseEntity = createTestCase();

        when(service.create(any(Case.class))).thenReturn(caseEntity);

        mockMvc.perform(post("/cases/")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(caseEntity)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Review application"))
            .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void shouldGetAllCases() throws Exception {

        when(service.getAll()).thenReturn(List.of(createTestCase()));

        mockMvc.perform(get("/cases/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].title").value("Review application"));
    }

    @Test
    void shouldGetCaseById() throws Exception {

        when(service.getById(1L)).thenReturn(createTestCase());

        mockMvc.perform(get("/cases/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Review application"));
    }

    @Test
    void shouldUpdateCase() throws Exception {

        Case updatedCase = createTestCase();

        updatedCase.setTitle("Updated application");
        updatedCase.setStatus(CaseStatus.COMPLETED);

        when(service.update(eq(1L), any(Case.class)))
            .thenReturn(updatedCase);

        mockMvc.perform(put("/cases/1")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(updatedCase)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Updated application"))
            .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void shouldDeleteCase() throws Exception {

        mockMvc.perform(delete("/cases/1"))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));

        verify(service).delete(1L);
    }

    @Test
    void shouldReturn400WhenTitleIsBlank() throws Exception {

        Case invalidCase = createTestCase();
        invalidCase.setTitle("");

        mockMvc.perform(post("/cases/")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(invalidCase)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.messages.title")
                           .value("Title is required"));

        verify(service, never()).create(any(Case.class));
    }

    @Test
    void shouldAllowMissingDescription() throws Exception {

        Case caseEntity = createTestCase();
        caseEntity.setDescription(null);

        when(service.create(any(Case.class))).thenReturn(caseEntity);

        mockMvc.perform(post("/cases/")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(caseEntity)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.description").doesNotExist());
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingCase() throws Exception {

        doThrow(new CaseNotFoundException(99L))
            .when(service).delete(99L);

        mockMvc.perform(delete("/cases/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message")
                           .value("Case not found with id: 99"));

        verify(service).delete(99L);
    }

    @Test
    void shouldReturn404WhenCaseDoesNotExist() throws Exception {

        when(service.getById(99L))
            .thenThrow(new CaseNotFoundException(99L));

        mockMvc.perform(get("/cases/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message")
                           .value("Case not found with id: 99"));
    }

    @Test
    void shouldReturn400WhenStatusIsMissing() throws Exception {

        Case invalidCase = createTestCase();
        invalidCase.setStatus(null);

        mockMvc.perform(post("/cases/")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(invalidCase)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.messages.status")
                           .value("Status is required"));

        verify(service, never()).create(any(Case.class));
    }

    @Test
    void shouldReturn400WhenDueDateIsMissing() throws Exception {

        Case invalidCase = createTestCase();
        invalidCase.setDueDate(null);

        mockMvc.perform(post("/cases/")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(invalidCase)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.messages.dueDate")
                           .value("Due date and time are required"));

        verify(service, never()).create(any(Case.class));
    }

    @Test
    void shouldReturn400WhenStatusIsInvalid() throws Exception {

        String json = """
            {
                "title": "Review application",
                "description": "Review submitted documents",
                "status": "INVALID",
                "dueDate": "2026-11-15T17:00:00"
            }
            """;

        mockMvc.perform(post("/cases/")
                            .contentType("application/json")
                            .content(json))
            .andExpect(status().isBadRequest());

        verify(service, never()).create(any(Case.class));
    }

}
