package com.example.ResultRush.MockMvcTests;

import com.example.ResultRush.controller.CategoryController;
import com.example.ResultRush.controller.GoalController;
import com.example.ResultRush.dto.CategoryDto;
import com.example.ResultRush.dto.GoalDto;
import com.example.ResultRush.model.AllGoalsStat;
import com.example.ResultRush.service.CategoryService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private CategoryController categoryController;
    private MockMvc mvc;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(categoryController)
                .build();

        categoryDto = CategoryDto.builder()
                .id(1)
                .title("title")
                .color("#fff")
                .build();
    }

    @Test
    void createCategory() throws Exception {

        Mockito.when(categoryService.create(Mockito.any()))
                .thenReturn(categoryDto);

        mvc.perform(post("/categories")
                        .content(mapper.writeValueAsString(categoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDto.getId())));
    }

    @Test
    void updateCategory() throws Exception {

        Mockito.when(categoryService.update(Mockito.any()))
                .thenReturn(categoryDto);

        mvc.perform(patch("/categories")
                        .content(mapper.writeValueAsString(categoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDto.getId())));
    }

    @Test
    void getCategoryById() throws Exception {

        Mockito.when(categoryService.getById(Mockito.any()))
                .thenReturn(categoryDto);

        mvc.perform(get("/categories/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDto.getId())));
    }

    @Test
    void getAll() throws Exception {

        Mockito.when(categoryService.getAll())
                .thenReturn(List.of(categoryDto));

        mvc.perform(get("/categories")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void deleteCategory() throws Exception {
        Mockito.doNothing().when(categoryService).delete(Mockito.any());

        mvc.perform(delete("/categories/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}