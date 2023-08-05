package com.example.ResultRush.MockMvcTests;

import com.example.ResultRush.controller.CategoryController;
import com.example.ResultRush.controller.PriorityController;
import com.example.ResultRush.dto.CategoryDto;
import com.example.ResultRush.dto.PriorityDto;
import com.example.ResultRush.service.CategoryService;
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
public class PriorityControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private PriorityService priorityService;
    @InjectMocks
    private PriorityController priorityController;
    private MockMvc mvc;
    private PriorityDto priorityDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(priorityController)
                .build();

        priorityDto = PriorityDto.builder()
                .id(1)
                .title("title")
                .build();
    }

    @Test
    void createPriority() throws Exception {

        Mockito.when(priorityService.create(Mockito.any()))
                .thenReturn(priorityDto);

        mvc.perform(post("/priorities")
                        .content(mapper.writeValueAsString(priorityDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(priorityDto.getId())));
    }

    @Test
    void updatePriority() throws Exception {

        Mockito.when(priorityService.update(Mockito.any()))
                .thenReturn(priorityDto);

        mvc.perform(patch("/priorities")
                        .content(mapper.writeValueAsString(priorityDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(priorityDto.getId())));
    }

    @Test
    void getPriorityById() throws Exception {

        Mockito.when(priorityService.getById(Mockito.any()))
                .thenReturn(priorityDto);

        mvc.perform(get("/priorities/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(priorityDto.getId())));
    }

    @Test
    void getAll() throws Exception {

        Mockito.when(priorityService.getAll())
                .thenReturn(List.of(priorityDto));

        mvc.perform(get("/priorities")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void deletePriority() throws Exception {

        Mockito.doNothing().when(priorityService).delete(Mockito.any());

        mvc.perform(delete("/priorities/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}