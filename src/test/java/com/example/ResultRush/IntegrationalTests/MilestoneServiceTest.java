package com.example.ResultRush.IntegrationalTests;

import com.example.ResultRush.configurations.UserPrincipal;
import com.example.ResultRush.dto.MilestoneDto;
import com.example.ResultRush.entity.Goal;
import com.example.ResultRush.entity.Milestone;
import com.example.ResultRush.entity.Usr;
import com.example.ResultRush.service.MilestoneService;
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
public class MilestoneServiceTest {

    private final EntityManager em;
    private final MilestoneService milestoneService;

    @Test
    @WithMockCustomUser
    public void createMilestoneSuccess() {
        var user = createUser();
        var goal = createGoal();
        goal.setUser(user);
        em.persist(user);
        em.persist(goal);
        setPrincipal(user);

        var milestone = MilestoneDto.toMilestoneDto(createMilestone());
        milestone.setGoalId(goal.getId());

        milestone = milestoneService.create(milestone);

        TypedQuery<Milestone> query = em.createQuery("Select m from Milestone m where m.id = :id", Milestone.class);
        var dbResult = query.setParameter("id", milestone.getId()).getResultList();

        Assertions.assertEquals(1, dbResult.size());
        var dbMilestone = dbResult.get(0);
        Assertions.assertEquals(milestone.getTitle(), dbMilestone.getTitle());
        Assertions.assertEquals(goal.getId(), dbMilestone.getGoal().getId());
    }

    @Test
    @WithMockCustomUser
    public void createMilestoneBlankFail() {
        var user = createUser();
        var goal = createGoal();
        goal.setUser(user);
        em.persist(user);
        em.persist(goal);
        setPrincipal(user);

        var milestone = MilestoneDto.toMilestoneDto(createMilestone());
        milestone.setGoalId(goal.getId());
        milestone.setTitle("   ");

        Assertions.assertThrows(Exception.class, () -> milestoneService.create(milestone));

        milestone.setTitle("new title");
        milestone.setGoalId(null);
        Assertions.assertThrows(Exception.class, () -> milestoneService.create(milestone));
    }

    @Test
    @WithMockCustomUser
    public void updateMilestoneSuccess() {
        var user = createUser();
        var goal = createGoal();
        var milestone = createMilestone();
        goal.setUser(user);
        milestone.setGoal(goal);
        em.persist(user);
        em.persist(goal);
        em.persist(milestone);
        setPrincipal(user);

        var milestoneUpdate = MilestoneDto.toMilestoneDto(createMilestone());
        milestoneUpdate.setId(milestone.getId());
        milestoneUpdate.setGoalId(goal.getId());

        milestoneService.update(milestoneUpdate);

        TypedQuery<Milestone> query = em.createQuery("Select m from Milestone m where m.id = :id", Milestone.class);
        var dbResult = query.setParameter("id", milestone.getId()).getSingleResult();

        Assertions.assertEquals(milestoneUpdate.getTitle(), dbResult.getTitle());
        Assertions.assertEquals(milestoneUpdate.getGoalId(), dbResult.getGoal().getId());
        Assertions.assertEquals(milestoneUpdate.getIsCompleted(), dbResult.getIsCompleted());
    }

    @Test
    @WithMockCustomUser
    public void updateMilestoneByStrangerFail() {
        var user = createUser();
        var goal = createGoal();
        var milestone = createMilestone();
        goal.setUser(user);
        milestone.setGoal(goal);
        em.persist(user);
        em.persist(goal);
        em.persist(milestone);

        var milestoneUpdate = MilestoneDto.toMilestoneDto(createMilestone());
        milestoneUpdate.setId(milestone.getId());
        milestoneUpdate.setGoalId(goal.getId());

        var stranger = createUser();
        setPrincipal(stranger);

        Assertions.assertThrows(Exception.class, () -> milestoneService.update(milestoneUpdate));
    }

    @Test
    @WithMockCustomUser
    public void getByIdSuccess() {
        var user = createUser();
        var goal = createGoal();
        var milestone = createMilestone();
        goal.setUser(user);
        milestone.setGoal(goal);
        em.persist(user);
        em.persist(goal);
        em.persist(milestone);
        setPrincipal(user);

        var milestones = milestoneService.getById(milestone.getId());

        TypedQuery<Milestone> query = em.createQuery("Select m from Milestone m where m.id = :id", Milestone.class);
        var dbResult = query.setParameter("id", milestone.getId()).getSingleResult();
        Assertions.assertEquals(milestone.getTitle(), dbResult.getTitle());
    }

    @Test
    @WithMockCustomUser
    public void getByIdByStrangerFail() {
        var user = createUser();
        var goal = createGoal();
        var milestone = createMilestone();
        goal.setUser(user);
        milestone.setGoal(goal);
        em.persist(user);
        em.persist(goal);
        em.persist(milestone);

        var stranger = createUser();
        em.persist(stranger);
        setPrincipal(stranger);

        Assertions.assertThrows(Exception.class, () -> milestoneService.getById(milestone.getId()));
    }

    @Test
    @WithMockCustomUser
    public void finAllByGoalIdSuccess() {
        var user = createUser();
        var goal1 = createGoal();
        var goal2 = createGoal();
        var milestone1 = createMilestone();
        var milestone2 = createMilestone();
        goal1.setUser(user);
        goal2.setUser(user);
        milestone1.setGoal(goal1);
        milestone2.setGoal(goal1);
        em.persist(user);
        em.persist(goal1);
        em.persist(goal2);
        em.persist(milestone1);
        em.persist(milestone2);
        setPrincipal(user);

        var milestones = milestoneService.findAllByGoalId(goal1.getId());

        Assertions.assertEquals(2, milestones.size());
        Assertions.assertEquals(milestone1.getTitle(), milestones.get(0).getTitle());
        Assertions.assertEquals(milestone1.getGoal().getId(), milestones.get(0).getGoalId());

        Assertions.assertEquals(milestone2.getTitle(), milestones.get(1).getTitle());
        Assertions.assertEquals(milestone2.getGoal().getId(), milestones.get(1).getGoalId());

        milestones = milestoneService.findAllByGoalId(goal2.getId());
        Assertions.assertEquals(0, milestones.size());
    }

    @Test
    @WithMockCustomUser
    public void deleteByOwnerSuccess() {
        var user = createUser();
        var goal1 = createGoal();
        var milestone1 = createMilestone();
        goal1.setUser(user);
        milestone1.setGoal(goal1);
        em.persist(user);
        em.persist(goal1);
        em.persist(milestone1);
        setPrincipal(user);

        milestoneService.delete(milestone1.getId());

        TypedQuery<Milestone> query = em.createQuery("Select m from Milestone m where m.id = :id", Milestone.class);
        var dbResult = query.setParameter("id", milestone1.getId()).getResultList();
        Assertions.assertEquals(0, dbResult.size());
    }

    @Test
    @WithMockCustomUser
    public void deleteByStrangerFail() {
        var user = createUser();
        var goal1 = createGoal();
        var milestone1 = createMilestone();
        goal1.setUser(user);
        milestone1.setGoal(goal1);
        em.persist(user);
        em.persist(goal1);
        em.persist(milestone1);

        var stranger = createUser();
        em.persist(stranger);
        setPrincipal(stranger);

        Assertions.assertThrows(Exception.class, () -> milestoneService.delete(milestone1.getId()));
    }

    private void setPrincipal(Usr user) {
        ((UserPrincipal) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).setId(user.getId());
    }
}