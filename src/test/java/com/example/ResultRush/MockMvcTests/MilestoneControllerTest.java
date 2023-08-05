package com.example.ResultRush.MockMvcTests;

import com.example.ResultRush.controller.MilestoneController;
import com.example.ResultRush.controller.PriorityController;
import com.example.ResultRush.dto.MilestoneDto;
import com.example.ResultRush.dto.PriorityDto;
import com.example.ResultRush.service.MilestoneService;
import com.example.ResultRush.service.PriorityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MilestoneControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private MilestoneService milestoneService;
    @InjectMocks
    private MilestoneController milestoneController;
    private MockMvc mvc;
    private MilestoneDto milestoneDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(milestoneController)
                .build();

        milestoneDto = MilestoneDto.builder()
                .id(1)
                .title("title")
                .isCompleted(false)
                .build();
    }

    @Test
    void updateMilestone() throws Exception {

        Mockito.when(milestoneService.update(Mockito.any()))
                .thenReturn(milestoneDto);

        mvc.perform(patch("/milestones")
                        .content(mapper.writeValueAsString(milestoneDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(milestoneDto.getId())));
    }

    @Test
    void getMilestoneById() throws Exception {

        Mockito.when(milestoneService.getById(Mockito.any()))
                .thenReturn(milestoneDto);

        mvc.perform(get("/milestones/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(milestoneDto.getId())));
    }

    @Test
    void getAllMilestones() throws Exception {

        Mockito.when(milestoneService.findAllByGoalId(Mockito.any()))
                .thenReturn(List.of(milestoneDto));

        mvc.perform(get("/milestones/goal/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void deleteMilestone() throws Exception {

        Mockito.doNothing().when(milestoneService).delete(Mockito.any());

        mvc.perform(delete("/milestones/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}