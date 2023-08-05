package com.example.ResultRush.IntegrationalTests;

import com.example.ResultRush.configurations.UserPrincipal;
import com.example.ResultRush.dto.CategoryDto;
import com.example.ResultRush.dto.PriorityDto;
import com.example.ResultRush.entity.Category;
import com.example.ResultRush.entity.Priority;
import com.example.ResultRush.entity.Usr;
import com.example.ResultRush.service.CategoryService;
import com.example.ResultRush.service.PriorityService;
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
import static com.example.ResultRush.IntegrationalTests.util.InstanceCreator.createUser;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class PriorityServiceTest {

    private final EntityManager em;
    private final PriorityService priorityService;

    @Test
    @WithMockCustomUser
    void createSuccess() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var priority = createPriority();

        var priorityDto = priorityService.create(PriorityDto.toPriorityDto(priority));

        TypedQuery<Priority> query = em.createQuery("Select p from Priority p where p.id = :id", Priority.class);
        var dbResult = query.setParameter("id", priorityDto.getId()).getResultList();

        Assertions.assertEquals(1, dbResult.size());
        var dbPriority = dbResult.get(0);
        Assertions.assertEquals(priorityDto.getTitle(), dbPriority.getTitle());
        Assertions.assertEquals(user.getId(), dbPriority.getUser().getId());
    }

    @Test
    @WithMockCustomUser
    void createEmptyTitleFail() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var priority = createPriority();
        priority.setTitle("    ");

        Assertions.assertThrows(Exception.class, () -> priorityService.create(PriorityDto.toPriorityDto(priority)));
    }

    @Test
    @WithMockCustomUser
    void getAllPrioritiesSuccess() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var priority1 = createPriority();
        var priority2 = createPriority();
        priority1.setUser(user);
        priority2.setUser(user);

        em.persist(priority1);
        em.persist(priority2);

        var result = priorityService.getAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(priority1.getTitle(), result.get(0).getTitle());
        Assertions.assertEquals(priority2.getTitle(), result.get(1).getTitle());

        var newUser = createUser();
        em.persist(newUser);
        setPrincipal(newUser);
        Assertions.assertEquals(0, priorityService.getAll().size());
    }

    @Test
    @WithMockCustomUser
    void getPriorityByIdSuccess() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var priority = createPriority();
        priority.setUser(user);
        em.persist(priority);

        var serviceResult = priorityService.getById(priority.getId());

        Assertions.assertEquals(priority.getTitle(), serviceResult.getTitle());

        var newUser = createUser();
        em.persist(newUser);
        setPrincipal(newUser);
        Assertions.assertThrows(Exception.class, () -> priorityService.getById(priority.getId()));
    }

    @Test
    @WithMockCustomUser
    void updatePrioritySuccess() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var priority = createPriority();
        priority.setUser(user);
        em.persist(priority);

        var priorityUpdate = PriorityDto.toPriorityDto(createPriority());
        priorityUpdate.setId(priority.getId());

        priorityService.update(priorityUpdate);

        TypedQuery<Priority> query = em.createQuery("Select p from Priority p where p.id = :id", Priority.class);
        var dbResult = query.setParameter("id", priority.getId()).getSingleResult();

        Assertions.assertEquals(priorityUpdate.getTitle(), dbResult.getTitle());
        Assertions.assertEquals(user.getId(), dbResult.getUser().getId());
    }

    @Test
    @WithMockCustomUser
    void updatePriorityFail() {
        var user = createUser();
        em.persist(user);

        var priority = createPriority();
        priority.setUser(user);
        em.persist(priority);

        var priorityUpdate = PriorityDto.toPriorityDto(createPriority());
        priorityUpdate.setId(priority.getId());

        var stranger = createUser();
        em.persist(stranger);
        setPrincipal(stranger);

        Assertions.assertThrows(Exception.class, () -> priorityService.update(priorityUpdate));
    }

    @Test
    @WithMockCustomUser
    void deletePrioritySuccess() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var priority = createPriority();
        priority.setUser(user);
        em.persist(priority);

        priorityService.delete(priority.getId());

        TypedQuery<Category> query = em.createQuery("Select p from Priority p where p.id = :id", Category.class);
        var dbResult = query.setParameter("id", priority.getId()).getResultList();
        Assertions.assertEquals(0, dbResult.size());
    }

    @Test
    @WithMockCustomUser
    void deletePriorityFail() {
        var user = createUser();
        em.persist(user);

        var priority = createPriority();
        priority.setUser(user);
        em.persist(priority);

        var stranger = createUser();
        em.persist(stranger);
        setPrincipal(stranger);

        Assertions.assertThrows(Exception.class, () -> priorityService.delete(priority.getId()));
    }


    private Integer getPrincipalId() {
        return ((UserPrincipal) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getId();
    }

    private void setPrincipal(Usr user) {
        ((UserPrincipal) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).setId(user.getId());
    }
}