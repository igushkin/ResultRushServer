package com.example.ResultRush.MockMvcTests;

import com.example.ResultRush.controller.GoalController;
import com.example.ResultRush.dto.GoalDto;
import com.example.ResultRush.model.AllGoalsStat;
import com.example.ResultRush.service.GoalService;
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
public class GoalControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private GoalService goalService;
    @InjectMocks
    private GoalController goalController;
    private MockMvc mvc;
    private AllGoalsStat goalsStat;
    private GoalDto goalDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(goalController)
                .build();

        goalDto = GoalDto.builder()
                .id(1)
                .title("title")
                .description("desc")
                .isCompleted(false)
                .build();

        goalsStat = new AllGoalsStat(1, 1);
    }

    @Test
    void createGoal() throws Exception {

        Mockito.when(goalService.create(Mockito.any()))
                .thenReturn(goalDto);

        mvc.perform(post("/goals")
                        .content(mapper.writeValueAsString(goalDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(goalDto.getId())));
    }

    @Test
    void updateGoal() throws Exception {

        Mockito.when(goalService.update(Mockito.any()))
                .thenReturn(goalDto);

        mvc.perform(patch("/goals")
                        .content(mapper.writeValueAsString(goalDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(goalDto.getId())));
    }

    @Test
    void getGoal() throws Exception {

        Mockito.when(goalService.getById(Mockito.any()))
                .thenReturn(goalDto);

        mvc.perform(get("/goals/1")
                        .content(mapper.writeValueAsString(goalDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(goalDto.getId())));
    }

    @Test
    void getAll() throws Exception {

        Mockito.when(goalService.getAll())
                .thenReturn(List.of(goalDto));

        mvc.perform(get("/goals")
                        .content(mapper.writeValueAsString(goalDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void deleteGoal() throws Exception {

        Mockito.doNothing().when(goalService).delete(Mockito.any());

        mvc.perform(delete("/goals/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getStat() throws Exception {
        Mockito.when(goalService.getStat())
                .thenReturn(goalsStat);

        mvc.perform(get("/goals/stat")
                        .content(mapper.writeValueAsString(goalsStat))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}