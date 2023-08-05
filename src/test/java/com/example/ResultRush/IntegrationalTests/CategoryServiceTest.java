package com.example.ResultRush.IntegrationalTests;

import com.example.ResultRush.configurations.UserPrincipal;
import com.example.ResultRush.dto.CategoryDto;
import com.example.ResultRush.entity.Category;
import com.example.ResultRush.entity.Usr;
import com.example.ResultRush.service.CategoryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static com.example.ResultRush.IntegrationalTests.util.InstanceCreator.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class CategoryServiceTest {
    private final EntityManager em;
    private final CategoryService categoryService;
    private static Integer counter = 0;

    @Test
    @WithMockCustomUser
    void createSuccess() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var category = CategoryDto.builder()
                .title("category")
                .color("#fff")
                .completedGoals(0)
                .uncompletedGoals(0)
                .totalGoals(0)
                .build();

        category = categoryService.create(category);

        TypedQuery<Category> query = em.createQuery("Select c from Category c where c.id = :id", Category.class);
        var dbResult = query.setParameter("id", category.getId()).getResultList();

        Assertions.assertEquals(1, dbResult.size());
        var dbCategory = dbResult.get(0);
        Assertions.assertEquals(category.getTitle(), dbCategory.getTitle());
        Assertions.assertEquals(category.getColor(), dbCategory.getColor());
        Assertions.assertEquals(user.getId(), dbCategory.getUser().getId());
    }

    @Test
    @WithMockCustomUser
    void createEmptyTitleFail() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var category = CategoryDto.builder()
                .title("    ")
                .color("#fff")
                .completedGoals(0)
                .uncompletedGoals(0)
                .totalGoals(0)
                .build();

        Assertions.assertThrows(Exception.class, () -> categoryService.create(category));
    }

    @Test
    @WithMockCustomUser
    void getAllCategoriesSuccess() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var category1 = Category.builder()
                .title("title 1")
                .color("#fff")
                .completedGoals(0)
                .uncompletedGoals(0)
                .totalGoals(0)
                .user(user)
                .build();

        var category2 = Category.builder()
                .title("title 2")
                .color("#000")
                .completedGoals(0)
                .uncompletedGoals(0)
                .totalGoals(0)
                .user(user)
                .build();

        em.persist(category1);
        em.persist(category2);

        var result = categoryService.getAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(category1.getTitle(), result.get(0).getTitle());
        Assertions.assertEquals(category2.getTitle(), result.get(1).getTitle());

        var newUser = createUser();
        em.persist(newUser);
        setPrincipal(newUser);
        Assertions.assertEquals(0, categoryService.getAll().size());
    }

    @Test
    @WithMockCustomUser
    void getAllCategoriesWithStatSuccess() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var category = Category.builder()
                .title("title 1")
                .color("#fff")
                .completedGoals(0)
                .uncompletedGoals(0)
                .totalGoals(0)
                .user(user)
                .build();

        var goal1 = createGoal();
        goal1.setUser(user);
        goal1.setCategory(category);
        goal1.setIsCompleted(true);

        var goal2 = createGoal();
        goal2.setUser(user);
        goal2.setCategory(category);
        goal2.setIsCompleted(false);

        em.persist(category);
        em.persist(goal1);
        em.persist(goal2);

        var result = categoryService.getAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1, result.get(0).getCompletedGoals());
        Assertions.assertEquals(1, result.get(0).getUncompletedGoals());
        Assertions.assertEquals(2, result.get(0).getTotalGoals());
    }

    @Test
    @WithMockCustomUser
    void getCategoryWithStatSuccess() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var category = Category.builder()
                .title("title 1")
                .color("#fff")
                .completedGoals(0)
                .uncompletedGoals(0)
                .totalGoals(0)
                .user(user)
                .build();

        var goal1 = createGoal();
        goal1.setUser(user);
        goal1.setCategory(category);
        goal1.setIsCompleted(true);

        var goal2 = createGoal();
        goal2.setUser(user);
        goal2.setCategory(category);
        goal2.setIsCompleted(false);

        em.persist(category);
        em.persist(goal1);
        em.persist(goal2);

        var result = categoryService.getById(category.getId());

        Assertions.assertEquals(1, result.getCompletedGoals());
        Assertions.assertEquals(1, result.getUncompletedGoals());
        Assertions.assertEquals(2, result.getTotalGoals());
    }

    @Test
    @WithMockCustomUser
    void getByIdSuccess() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var category = Category.builder()
                .title("title 1")
                .color("#fff")
                .completedGoals(0)
                .uncompletedGoals(0)
                .totalGoals(0)
                .user(user)
                .build();

        em.persist(category);

        var result = categoryService.getById(category.getId());

        Assertions.assertEquals(category.getTitle(), result.getTitle());
        Assertions.assertEquals(category.getColor(), result.getColor());
        Assertions.assertEquals(0, result.getTotalGoals());
        Assertions.assertEquals(0, result.getCompletedGoals());
        Assertions.assertEquals(0, result.getUncompletedGoals());

        var newUser = createUser();
        em.persist(newUser);
        setPrincipal(newUser);
        Assertions.assertThrows(Exception.class, () -> categoryService.getById(category.getId()));
    }

    @Test
    @WithMockCustomUser
    void updateCategorySuccess() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var category = Category.builder()
                .title("title 1")
                .color("#fff")
                .completedGoals(0)
                .uncompletedGoals(0)
                .totalGoals(0)
                .user(user)
                .build();

        em.persist(category);

        var categoryUpdate = CategoryDto.toCategoryDto(createCategory());
        categoryUpdate.setId(category.getId());

        categoryService.update(categoryUpdate);

        TypedQuery<Category> query = em.createQuery("Select c from Category c where c.id = :id", Category.class);
        var dbResult = query.setParameter("id", category.getId()).getSingleResult();


        Assertions.assertEquals(categoryUpdate.getTitle(), dbResult.getTitle());
        Assertions.assertEquals(categoryUpdate.getColor(), dbResult.getColor());
        Assertions.assertEquals(user.getId(), dbResult.getUser().getId());
    }

    @Test
    @WithMockCustomUser
    void updateCategoryFail() {
        var user = createUser();
        em.persist(user);

        var category = Category.builder()
                .title("title 1")
                .color("#fff")
                .completedGoals(0)
                .uncompletedGoals(0)
                .totalGoals(0)
                .user(user)
                .build();

        em.persist(category);

        var categoryUpdate = CategoryDto.toCategoryDto(createCategory());
        var stranger = createUser();
        em.persist(stranger);
        setPrincipal(stranger);

        categoryUpdate.setId(category.getId());
        Assertions.assertThrows(Exception.class, () -> categoryService.update(categoryUpdate));
    }

    @Test
    @WithMockCustomUser
    void deleteCategorySuccess() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var category = Category.builder()
                .title("title 1")
                .color("#fff")
                .completedGoals(0)
                .uncompletedGoals(0)
                .totalGoals(0)
                .user(user)
                .build();

        em.persist(category);

        categoryService.delete(category.getId());

        TypedQuery<Category> query = em.createQuery("Select c from Category c where c.id = :id", Category.class);
        var dbResult = query.setParameter("id", category.getId()).getResultList();
        Assertions.assertEquals(0, dbResult.size());
    }

    @Test
    @WithMockCustomUser
    void deleteCategoryFail() {
        var user = createUser();
        em.persist(user);

        var category = Category.builder()
                .title("title 1")
                .color("#fff")
                .completedGoals(0)
                .uncompletedGoals(0)
                .totalGoals(0)
                .user(user)
                .build();

        em.persist(category);
        var stranger = createUser();
        em.persist(stranger);
        setPrincipal(stranger);

        Assertions.assertThrows(Exception.class, () -> categoryService.delete(category.getId()));
    }


    private Integer getPrincipalId() {
        return ((UserPrincipal) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getId();
    }

    private void setPrincipal(Usr user) {
        ((UserPrincipal) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).setId(user.getId());
    }
}