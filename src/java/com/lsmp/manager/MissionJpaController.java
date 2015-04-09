/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.manager;

import com.lsmp.manager.exceptions.IllegalOrphanException;
import com.lsmp.manager.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.lsmp.model.TypeMission;
import com.lsmp.model.Utilisateur;
import com.lsmp.model.Infrastructure;
import java.util.ArrayList;
import java.util.List;
import com.lsmp.model.Intervention;
import com.lsmp.model.Mission;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TiDy
 */
public class MissionJpaController implements Serializable {

    public MissionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Mission mission) {
        if (mission.getInfrastructureList() == null) {
            mission.setInfrastructureList(new ArrayList<Infrastructure>());
        }
        if (mission.getInterventionList() == null) {
            mission.setInterventionList(new ArrayList<Intervention>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TypeMission missionType = mission.getMissionType();
            if (missionType != null) {
                missionType = em.getReference(missionType.getClass(), missionType.getIdTypeMission());
                mission.setMissionType(missionType);
            }
            Utilisateur demandeurId = mission.getDemandeurId();
            if (demandeurId != null) {
                demandeurId = em.getReference(demandeurId.getClass(), demandeurId.getIdUtilisateur());
                mission.setDemandeurId(demandeurId);
            }
            Utilisateur intervenantId = mission.getIntervenantId();
            if (intervenantId != null) {
                intervenantId = em.getReference(intervenantId.getClass(), intervenantId.getIdUtilisateur());
                mission.setIntervenantId(intervenantId);
            }
            List<Infrastructure> attachedInfrastructureList = new ArrayList<Infrastructure>();
            for (Infrastructure infrastructureListInfrastructureToAttach : mission.getInfrastructureList()) {
                infrastructureListInfrastructureToAttach = em.getReference(infrastructureListInfrastructureToAttach.getClass(), infrastructureListInfrastructureToAttach.getIdInfrastructure());
                attachedInfrastructureList.add(infrastructureListInfrastructureToAttach);
            }
            mission.setInfrastructureList(attachedInfrastructureList);
            List<Intervention> attachedInterventionList = new ArrayList<Intervention>();
            for (Intervention interventionListInterventionToAttach : mission.getInterventionList()) {
                interventionListInterventionToAttach = em.getReference(interventionListInterventionToAttach.getClass(), interventionListInterventionToAttach.getIdIntervention());
                attachedInterventionList.add(interventionListInterventionToAttach);
            }
            mission.setInterventionList(attachedInterventionList);
            em.persist(mission);
            if (missionType != null) {
                missionType.getMissionList().add(mission);
                missionType = em.merge(missionType);
            }
            if (demandeurId != null) {
                demandeurId.getMissionList().add(mission);
                demandeurId = em.merge(demandeurId);
            }
            if (intervenantId != null) {
                intervenantId.getMissionList().add(mission);
                intervenantId = em.merge(intervenantId);
            }
            for (Infrastructure infrastructureListInfrastructure : mission.getInfrastructureList()) {
                infrastructureListInfrastructure.getMissionList().add(mission);
                infrastructureListInfrastructure = em.merge(infrastructureListInfrastructure);
            }
            for (Intervention interventionListIntervention : mission.getInterventionList()) {
                Mission oldInterventionMissionOfInterventionListIntervention = interventionListIntervention.getInterventionMission();
                interventionListIntervention.setInterventionMission(mission);
                interventionListIntervention = em.merge(interventionListIntervention);
                if (oldInterventionMissionOfInterventionListIntervention != null) {
                    oldInterventionMissionOfInterventionListIntervention.getInterventionList().remove(interventionListIntervention);
                    oldInterventionMissionOfInterventionListIntervention = em.merge(oldInterventionMissionOfInterventionListIntervention);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Mission mission) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mission persistentMission = em.find(Mission.class, mission.getIdMission());
            TypeMission missionTypeOld = persistentMission.getMissionType();
            TypeMission missionTypeNew = mission.getMissionType();
            Utilisateur demandeurIdOld = persistentMission.getDemandeurId();
            Utilisateur demandeurIdNew = mission.getDemandeurId();
            Utilisateur intervenantIdOld = persistentMission.getIntervenantId();
            Utilisateur intervenantIdNew = mission.getIntervenantId();
            List<Infrastructure> infrastructureListOld = persistentMission.getInfrastructureList();
            List<Infrastructure> infrastructureListNew = mission.getInfrastructureList();
            List<Intervention> interventionListOld = persistentMission.getInterventionList();
            List<Intervention> interventionListNew = mission.getInterventionList();
            List<String> illegalOrphanMessages = null;
            for (Intervention interventionListOldIntervention : interventionListOld) {
                if (!interventionListNew.contains(interventionListOldIntervention)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Intervention " + interventionListOldIntervention + " since its interventionMission field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (missionTypeNew != null) {
                missionTypeNew = em.getReference(missionTypeNew.getClass(), missionTypeNew.getIdTypeMission());
                mission.setMissionType(missionTypeNew);
            }
            if (demandeurIdNew != null) {
                demandeurIdNew = em.getReference(demandeurIdNew.getClass(), demandeurIdNew.getIdUtilisateur());
                mission.setDemandeurId(demandeurIdNew);
            }
            if (intervenantIdNew != null) {
                intervenantIdNew = em.getReference(intervenantIdNew.getClass(), intervenantIdNew.getIdUtilisateur());
                mission.setIntervenantId(intervenantIdNew);
            }
            List<Infrastructure> attachedInfrastructureListNew = new ArrayList<Infrastructure>();
            for (Infrastructure infrastructureListNewInfrastructureToAttach : infrastructureListNew) {
                infrastructureListNewInfrastructureToAttach = em.getReference(infrastructureListNewInfrastructureToAttach.getClass(), infrastructureListNewInfrastructureToAttach.getIdInfrastructure());
                attachedInfrastructureListNew.add(infrastructureListNewInfrastructureToAttach);
            }
            infrastructureListNew = attachedInfrastructureListNew;
            mission.setInfrastructureList(infrastructureListNew);
            List<Intervention> attachedInterventionListNew = new ArrayList<Intervention>();
            for (Intervention interventionListNewInterventionToAttach : interventionListNew) {
                interventionListNewInterventionToAttach = em.getReference(interventionListNewInterventionToAttach.getClass(), interventionListNewInterventionToAttach.getIdIntervention());
                attachedInterventionListNew.add(interventionListNewInterventionToAttach);
            }
            interventionListNew = attachedInterventionListNew;
            mission.setInterventionList(interventionListNew);
            mission = em.merge(mission);
            if (missionTypeOld != null && !missionTypeOld.equals(missionTypeNew)) {
                missionTypeOld.getMissionList().remove(mission);
                missionTypeOld = em.merge(missionTypeOld);
            }
            if (missionTypeNew != null && !missionTypeNew.equals(missionTypeOld)) {
                missionTypeNew.getMissionList().add(mission);
                missionTypeNew = em.merge(missionTypeNew);
            }
            if (demandeurIdOld != null && !demandeurIdOld.equals(demandeurIdNew)) {
                demandeurIdOld.getMissionList().remove(mission);
                demandeurIdOld = em.merge(demandeurIdOld);
            }
            if (demandeurIdNew != null && !demandeurIdNew.equals(demandeurIdOld)) {
                demandeurIdNew.getMissionList().add(mission);
                demandeurIdNew = em.merge(demandeurIdNew);
            }
            if (intervenantIdOld != null && !intervenantIdOld.equals(intervenantIdNew)) {
                intervenantIdOld.getMissionList().remove(mission);
                intervenantIdOld = em.merge(intervenantIdOld);
            }
            if (intervenantIdNew != null && !intervenantIdNew.equals(intervenantIdOld)) {
                intervenantIdNew.getMissionList().add(mission);
                intervenantIdNew = em.merge(intervenantIdNew);
            }
            for (Infrastructure infrastructureListOldInfrastructure : infrastructureListOld) {
                if (!infrastructureListNew.contains(infrastructureListOldInfrastructure)) {
                    infrastructureListOldInfrastructure.getMissionList().remove(mission);
                    infrastructureListOldInfrastructure = em.merge(infrastructureListOldInfrastructure);
                }
            }
            for (Infrastructure infrastructureListNewInfrastructure : infrastructureListNew) {
                if (!infrastructureListOld.contains(infrastructureListNewInfrastructure)) {
                    infrastructureListNewInfrastructure.getMissionList().add(mission);
                    infrastructureListNewInfrastructure = em.merge(infrastructureListNewInfrastructure);
                }
            }
            for (Intervention interventionListNewIntervention : interventionListNew) {
                if (!interventionListOld.contains(interventionListNewIntervention)) {
                    Mission oldInterventionMissionOfInterventionListNewIntervention = interventionListNewIntervention.getInterventionMission();
                    interventionListNewIntervention.setInterventionMission(mission);
                    interventionListNewIntervention = em.merge(interventionListNewIntervention);
                    if (oldInterventionMissionOfInterventionListNewIntervention != null && !oldInterventionMissionOfInterventionListNewIntervention.equals(mission)) {
                        oldInterventionMissionOfInterventionListNewIntervention.getInterventionList().remove(interventionListNewIntervention);
                        oldInterventionMissionOfInterventionListNewIntervention = em.merge(oldInterventionMissionOfInterventionListNewIntervention);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = mission.getIdMission();
                if (findMission(id) == null) {
                    throw new NonexistentEntityException("The mission with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mission mission;
            try {
                mission = em.getReference(Mission.class, id);
                mission.getIdMission();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mission with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Intervention> interventionListOrphanCheck = mission.getInterventionList();
            for (Intervention interventionListOrphanCheckIntervention : interventionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Mission (" + mission + ") cannot be destroyed since the Intervention " + interventionListOrphanCheckIntervention + " in its interventionList field has a non-nullable interventionMission field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TypeMission missionType = mission.getMissionType();
            if (missionType != null) {
                missionType.getMissionList().remove(mission);
                missionType = em.merge(missionType);
            }
            Utilisateur demandeurId = mission.getDemandeurId();
            if (demandeurId != null) {
                demandeurId.getMissionList().remove(mission);
                demandeurId = em.merge(demandeurId);
            }
            Utilisateur intervenantId = mission.getIntervenantId();
            if (intervenantId != null) {
                intervenantId.getMissionList().remove(mission);
                intervenantId = em.merge(intervenantId);
            }
            List<Infrastructure> infrastructureList = mission.getInfrastructureList();
            for (Infrastructure infrastructureListInfrastructure : infrastructureList) {
                infrastructureListInfrastructure.getMissionList().remove(mission);
                infrastructureListInfrastructure = em.merge(infrastructureListInfrastructure);
            }
            em.remove(mission);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Mission> findMissionEntities() {
        return findMissionEntities(true, -1, -1);
    }

    public List<Mission> findMissionEntities(int maxResults, int firstResult) {
        return findMissionEntities(false, maxResults, firstResult);
    }

    private List<Mission> findMissionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Mission.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Mission findMission(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mission.class, id);
        } finally {
            em.close();
        }
    }

    public int getMissionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Mission> rt = cq.from(Mission.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
