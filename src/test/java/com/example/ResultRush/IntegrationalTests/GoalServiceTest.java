package com.example.ResultRush.IntegrationalTests;

import com.example.ResultRush.configurations.UserPrincipal;
import com.example.ResultRush.dto.GoalDto;
import com.example.ResultRush.entity.Goal;
import com.example.ResultRush.entity.Usr;
import com.example.ResultRush.service.GoalService;
import com.example.ResultRush.service.UserService;
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
class GoalServiceTest {

    private final EntityManager em;
    private final GoalService goalService;
    private final UserService userService;
    private Integer counter = 0;

    @Test
    @WithMockCustomUser
    void createGoalSuccess() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var goal = GoalDto.builder()
                .title("title")
                .description("desc")
                .isCompleted(false)
                .build();

        goalService.create(goal);

        TypedQuery<Goal> query = em.createQuery("Select g from Goal g where g.title = :title", Goal.class);
        var dbResult = query.setParameter("title", goal.getTitle()).getResultList();

        Assertions.assertEquals(dbResult.size(), 1);
        var dbGoal = dbResult.get(0);
        Assertions.assertEquals(goal.getTitle(), dbGoal.getTitle());
        Assertions.assertEquals(goal.getDescription(), dbGoal.getDescription());
        Assertions.assertEquals(user.getId(), dbGoal.getUser().getId());
        Assertions.assertEquals(goal.getIsCompleted(), dbGoal.getIsCompleted());
    }

    @Test
    @WithMockCustomUser
    void createGoalWithPriorityAndCategorySuccess() {

        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var category = createCategory();
        category.setUser(user);
        var priority = createPriority();
        priority.setUser(user);
        em.persist(category);
        em.persist(priority);

        var goal = GoalDto.builder()
                .title("title")
                .description("desc")
                .isCompleted(false)
                .categoryId(category.getId())
                .priorityId(priority.getId())
                .build();

        goalService.create(goal);

        TypedQuery<Goal> query = em.createQuery("Select g from Goal g LEFT JOIN FETCH g.category LEFT JOIN FETCH g.priority where g.title = :title", Goal.class);
        var dbResult = query.setParameter("title", goal.getTitle()).getResultList();

        Assertions.assertEquals(dbResult.size(), 1);
        var dbGoal = dbResult.get(0);
        Assertions.assertEquals(goal.getCategoryId(), dbGoal.getCategory().getId());
        Assertions.assertEquals(goal.getPriorityId(), dbGoal.getPriority().getId());
    }

    @Test
    @WithMockCustomUser
    void createGoalWithNotExistingPriorityAndCategoryFail() {
        var categoryPriorityOwner = createUser();
        em.persist(categoryPriorityOwner);
        var category = createCategory();
        var priority = createPriority();
        category.setUser(categoryPriorityOwner);
        priority.setUser(categoryPriorityOwner);
        em.persist(category);
        em.persist(priority);

        var newUser = Usr.builder()
                .username("newUsername")
                .password("password")
                .build();

        em.persist(newUser);
        setPrincipal(newUser);

        var goal = GoalDto.builder()
                .title("title")
                .description("desc")
                .isCompleted(false)
                .categoryId(category.getId())
                .build();

        Assertions.assertThrows(Exception.class, () -> goalService.create(goal));

        goal.setCategoryId(null);
        goal.setPriorityId(priority.getId());

        Assertions.assertThrows(Exception.class, () -> goalService.create(goal));
    }

    @Test
    @WithMockCustomUser
    void updateGoalSuccess() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var goal = createGoal();
        var category = createCategory();
        var priority = createPriority();
        goal.setCategory(category);
        goal.setPriority(priority);
        goal.setUser(user);
        category.setUser(user);
        priority.setUser(user);
        em.persist(category);
        em.persist(priority);
        em.persist(goal);

        var goalUpdate = GoalDto.builder()
                .id(goal.getId())
                .title("New title")
                .description("New desc")
                .isCompleted(false)
                .build();

        goalService.update(goalUpdate);

        TypedQuery<Goal> query = em.createQuery("Select g from Goal g LEFT JOIN FETCH g.user LEFT JOIN FETCH g.priority LEFT JOIN FETCH g.category where g.id = :id", Goal.class);
        var goalDb = query.setParameter("id", goal.getId()).getSingleResult();

        Assertions.assertEquals(goalUpdate.getTitle(), goalDb.getTitle());
        Assertions.assertEquals(goalUpdate.getDescription(), goalDb.getDescription());
        Assertions.assertEquals(user.getId(), goalDb.getUser().getId());
        Assertions.assertEquals(goalUpdate.getIsCompleted(), goalDb.getIsCompleted());
        Assertions.assertEquals(null, goalDb.getCategory());
        Assertions.assertEquals(null, goalDb.getPriority());
    }

    @Test
    @WithMockCustomUser
    void updateGoalWithNotExistingCategoryAndPriorityFail() {
        var categoryPriorityOwner = createUser();
        em.persist(categoryPriorityOwner);

        var category = createCategory();
        var priority = createPriority();
        category.setUser(categoryPriorityOwner);
        priority.setUser(categoryPriorityOwner);
        em.persist(category);
        em.persist(priority);


        var goalOwner = createUser();
        em.persist(goalOwner);
        setPrincipal(goalOwner);

        var goal = createGoal();
        goal.setUser(goalOwner);
        em.persist(goal);

        var goalUpdate = GoalDto.builder()
                .id(goal.getId())
                .title("New title")
                .description("New desc")
                .categoryId(category.getId())
                .isCompleted(false)
                .build();


        Assertions.assertThrows(Exception.class, () -> goalService.update(goalUpdate));
        goalUpdate.setCategoryId(null);
        goalUpdate.setPriorityId(priority.getId());
        Assertions.assertThrows(Exception.class, () -> goalService.update(goalUpdate));
    }

    @Test
    @WithMockCustomUser
    void updateGoalByStrangerFail() {
        var goalOwner = createUser();
        em.persist(goalOwner);
        var stranger = createUser();
        setPrincipal(stranger);
        var goal = createGoal();
        goal.setUser(goalOwner);
        em.persist(goal);

        var goalUpdate = GoalDto.builder()
                .id(goal.getId())
                .title("New title")
                .description("New desc")
                .isCompleted(false)
                .build();

        Assertions.assertThrows(Exception.class, () -> goalService.update(goalUpdate));
    }

    @Test
    @WithMockCustomUser
    void deleteGoalSuccess() {

        var goalOwner = createUser();
        em.persist(goalOwner);
        setPrincipal(goalOwner);

        var goal = createGoal();
        goal.setUser(goalOwner);
        em.persist(goal);

        goalService.delete(goal.getId());

        Assertions.assertEquals(null, em.find(Goal.class, goal.getId()));
    }

    @Test
    @WithMockCustomUser
    void deleteGoalFail() {
        var goalOwner = createUser();
        em.persist(goalOwner);

        var goal = createGoal();
        goal.setUser(goalOwner);
        em.persist(goal);

        var stranger = createUser();
        em.persist(stranger);
        setPrincipal(stranger);

        Assertions.assertThrows(Exception.class, () -> goalService.delete(goal.getId()));
    }

    @Test
    @WithMockCustomUser
    void getGoalsStatSuccess() {
        var user = createUser();
        em.persist(user);
        setPrincipal(user);

        var res = goalService.getStat();

        Assertions.assertEquals(0, res.getCompletedGoals());
        Assertions.assertEquals(0, res.getUncompletedGoals());
        Assertions.assertEquals(0, res.getTotalGoals());

        var goal1 = createGoal();
        goal1.setUser(user);
        goal1.setIsCompleted(true);

        var goal2 = createGoal();
        goal2.setUser(user);
        goal2.setIsCompleted(false);

        em.persist(goal1);
        em.persist(goal2);

        res = goalService.getStat();

        Assertions.assertEquals(1, res.getCompletedGoals());
        Assertions.assertEquals(1, res.getUncompletedGoals());
        Assertions.assertEquals(2, res.getTotalGoals());

        var stranger = createUser();
        em.persist(stranger);
        setPrincipal(stranger);

        res = goalService.getStat();

        Assertions.assertEquals(0, res.getTotalGoals());
        Assertions.assertEquals(0, res.getCompletedGoals());
        Assertions.assertEquals(0, res.getUncompletedGoals());

    }

    private Integer getPrincipalId() {
        return ((UserPrincipal) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getId();
    }

    private void setPrincipal(Usr user) {
        ((UserPrincipal) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).setId(user.getId());
    }
}