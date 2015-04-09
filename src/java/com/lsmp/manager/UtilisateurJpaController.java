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
import com.lsmp.model.TypeUtilisateur;
import com.lsmp.model.Mission;
import java.util.ArrayList;
import java.util.List;
import com.lsmp.model.Intervention;
import com.lsmp.model.Utilisateur;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TiDy
 */
public class UtilisateurJpaController implements Serializable {

    public UtilisateurJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Utilisateur utilisateur) {
        if (utilisateur.getMissionList() == null) {
            utilisateur.setMissionList(new ArrayList<Mission>());
        }
        if (utilisateur.getMissionList1() == null) {
            utilisateur.setMissionList1(new ArrayList<Mission>());
        }
        if (utilisateur.getInterventionList() == null) {
            utilisateur.setInterventionList(new ArrayList<Intervention>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TypeUtilisateur utilisateurType = utilisateur.getUtilisateurType();
            if (utilisateurType != null) {
                utilisateurType = em.getReference(utilisateurType.getClass(), utilisateurType.getIdTypeUtilisateur());
                utilisateur.setUtilisateurType(utilisateurType);
            }
            List<Mission> attachedMissionList = new ArrayList<Mission>();
            for (Mission missionListMissionToAttach : utilisateur.getMissionList()) {
                missionListMissionToAttach = em.getReference(missionListMissionToAttach.getClass(), missionListMissionToAttach.getIdMission());
                attachedMissionList.add(missionListMissionToAttach);
            }
            utilisateur.setMissionList(attachedMissionList);
            List<Mission> attachedMissionList1 = new ArrayList<Mission>();
            for (Mission missionList1MissionToAttach : utilisateur.getMissionList1()) {
                missionList1MissionToAttach = em.getReference(missionList1MissionToAttach.getClass(), missionList1MissionToAttach.getIdMission());
                attachedMissionList1.add(missionList1MissionToAttach);
            }
            utilisateur.setMissionList1(attachedMissionList1);
            List<Intervention> attachedInterventionList = new ArrayList<Intervention>();
            for (Intervention interventionListInterventionToAttach : utilisateur.getInterventionList()) {
                interventionListInterventionToAttach = em.getReference(interventionListInterventionToAttach.getClass(), interventionListInterventionToAttach.getIdIntervention());
                attachedInterventionList.add(interventionListInterventionToAttach);
            }
            utilisateur.setInterventionList(attachedInterventionList);
            em.persist(utilisateur);
            if (utilisateurType != null) {
                utilisateurType.getUtilisateurList().add(utilisateur);
                utilisateurType = em.merge(utilisateurType);
            }
            for (Mission missionListMission : utilisateur.getMissionList()) {
                Utilisateur oldDemandeurIdOfMissionListMission = missionListMission.getDemandeurId();
                missionListMission.setDemandeurId(utilisateur);
                missionListMission = em.merge(missionListMission);
                if (oldDemandeurIdOfMissionListMission != null) {
                    oldDemandeurIdOfMissionListMission.getMissionList().remove(missionListMission);
                    oldDemandeurIdOfMissionListMission = em.merge(oldDemandeurIdOfMissionListMission);
                }
            }
            for (Mission missionList1Mission : utilisateur.getMissionList1()) {
                Utilisateur oldIntervenantIdOfMissionList1Mission = missionList1Mission.getIntervenantId();
                missionList1Mission.setIntervenantId(utilisateur);
                missionList1Mission = em.merge(missionList1Mission);
                if (oldIntervenantIdOfMissionList1Mission != null) {
                    oldIntervenantIdOfMissionList1Mission.getMissionList1().remove(missionList1Mission);
                    oldIntervenantIdOfMissionList1Mission = em.merge(oldIntervenantIdOfMissionList1Mission);
                }
            }
            for (Intervention interventionListIntervention : utilisateur.getInterventionList()) {
                Utilisateur oldIntervenantIdOfInterventionListIntervention = interventionListIntervention.getIntervenantId();
                interventionListIntervention.setIntervenantId(utilisateur);
                interventionListIntervention = em.merge(interventionListIntervention);
                if (oldIntervenantIdOfInterventionListIntervention != null) {
                    oldIntervenantIdOfInterventionListIntervention.getInterventionList().remove(interventionListIntervention);
                    oldIntervenantIdOfInterventionListIntervention = em.merge(oldIntervenantIdOfInterventionListIntervention);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Utilisateur utilisateur) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Utilisateur persistentUtilisateur = em.find(Utilisateur.class, utilisateur.getIdUtilisateur());
            TypeUtilisateur utilisateurTypeOld = persistentUtilisateur.getUtilisateurType();
            TypeUtilisateur utilisateurTypeNew = utilisateur.getUtilisateurType();
            List<Mission> missionListOld = persistentUtilisateur.getMissionList();
            List<Mission> missionListNew = utilisateur.getMissionList();
            List<Mission> missionList1Old = persistentUtilisateur.getMissionList1();
            List<Mission> missionList1New = utilisateur.getMissionList1();
            List<Intervention> interventionListOld = persistentUtilisateur.getInterventionList();
            List<Intervention> interventionListNew = utilisateur.getInterventionList();
            List<String> illegalOrphanMessages = null;
            for (Mission missionListOldMission : missionListOld) {
                if (!missionListNew.contains(missionListOldMission)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Mission " + missionListOldMission + " since its demandeurId field is not nullable.");
                }
            }
            for (Mission missionList1OldMission : missionList1Old) {
                if (!missionList1New.contains(missionList1OldMission)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Mission " + missionList1OldMission + " since its intervenantId field is not nullable.");
                }
            }
            for (Intervention interventionListOldIntervention : interventionListOld) {
                if (!interventionListNew.contains(interventionListOldIntervention)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Intervention " + interventionListOldIntervention + " since its intervenantId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (utilisateurTypeNew != null) {
                utilisateurTypeNew = em.getReference(utilisateurTypeNew.getClass(), utilisateurTypeNew.getIdTypeUtilisateur());
                utilisateur.setUtilisateurType(utilisateurTypeNew);
            }
            List<Mission> attachedMissionListNew = new ArrayList<Mission>();
            for (Mission missionListNewMissionToAttach : missionListNew) {
                missionListNewMissionToAttach = em.getReference(missionListNewMissionToAttach.getClass(), missionListNewMissionToAttach.getIdMission());
                attachedMissionListNew.add(missionListNewMissionToAttach);
            }
            missionListNew = attachedMissionListNew;
            utilisateur.setMissionList(missionListNew);
            List<Mission> attachedMissionList1New = new ArrayList<Mission>();
            for (Mission missionList1NewMissionToAttach : missionList1New) {
                missionList1NewMissionToAttach = em.getReference(missionList1NewMissionToAttach.getClass(), missionList1NewMissionToAttach.getIdMission());
                attachedMissionList1New.add(missionList1NewMissionToAttach);
            }
            missionList1New = attachedMissionList1New;
            utilisateur.setMissionList1(missionList1New);
            List<Intervention> attachedInterventionListNew = new ArrayList<Intervention>();
            for (Intervention interventionListNewInterventionToAttach : interventionListNew) {
                interventionListNewInterventionToAttach = em.getReference(interventionListNewInterventionToAttach.getClass(), interventionListNewInterventionToAttach.getIdIntervention());
                attachedInterventionListNew.add(interventionListNewInterventionToAttach);
            }
            interventionListNew = attachedInterventionListNew;
            utilisateur.setInterventionList(interventionListNew);
            utilisateur = em.merge(utilisateur);
            if (utilisateurTypeOld != null && !utilisateurTypeOld.equals(utilisateurTypeNew)) {
                utilisateurTypeOld.getUtilisateurList().remove(utilisateur);
                utilisateurTypeOld = em.merge(utilisateurTypeOld);
            }
            if (utilisateurTypeNew != null && !utilisateurTypeNew.equals(utilisateurTypeOld)) {
                utilisateurTypeNew.getUtilisateurList().add(utilisateur);
                utilisateurTypeNew = em.merge(utilisateurTypeNew);
            }
            for (Mission missionListNewMission : missionListNew) {
                if (!missionListOld.contains(missionListNewMission)) {
                    Utilisateur oldDemandeurIdOfMissionListNewMission = missionListNewMission.getDemandeurId();
                    missionListNewMission.setDemandeurId(utilisateur);
                    missionListNewMission = em.merge(missionListNewMission);
                    if (oldDemandeurIdOfMissionListNewMission != null && !oldDemandeurIdOfMissionListNewMission.equals(utilisateur)) {
                        oldDemandeurIdOfMissionListNewMission.getMissionList().remove(missionListNewMission);
                        oldDemandeurIdOfMissionListNewMission = em.merge(oldDemandeurIdOfMissionListNewMission);
                    }
                }
            }
            for (Mission missionList1NewMission : missionList1New) {
                if (!missionList1Old.contains(missionList1NewMission)) {
                    Utilisateur oldIntervenantIdOfMissionList1NewMission = missionList1NewMission.getIntervenantId();
                    missionList1NewMission.setIntervenantId(utilisateur);
                    missionList1NewMission = em.merge(missionList1NewMission);
                    if (oldIntervenantIdOfMissionList1NewMission != null && !oldIntervenantIdOfMissionList1NewMission.equals(utilisateur)) {
                        oldIntervenantIdOfMissionList1NewMission.getMissionList1().remove(missionList1NewMission);
                        oldIntervenantIdOfMissionList1NewMission = em.merge(oldIntervenantIdOfMissionList1NewMission);
                    }
                }
            }
            for (Intervention interventionListNewIntervention : interventionListNew) {
                if (!interventionListOld.contains(interventionListNewIntervention)) {
                    Utilisateur oldIntervenantIdOfInterventionListNewIntervention = interventionListNewIntervention.getIntervenantId();
                    interventionListNewIntervention.setIntervenantId(utilisateur);
                    interventionListNewIntervention = em.merge(interventionListNewIntervention);
                    if (oldIntervenantIdOfInterventionListNewIntervention != null && !oldIntervenantIdOfInterventionListNewIntervention.equals(utilisateur)) {
                        oldIntervenantIdOfInterventionListNewIntervention.getInterventionList().remove(interventionListNewIntervention);
                        oldIntervenantIdOfInterventionListNewIntervention = em.merge(oldIntervenantIdOfInterventionListNewIntervention);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = utilisateur.getIdUtilisateur();
                if (findUtilisateur(id) == null) {
                    throw new NonexistentEntityException("The utilisateur with id " + id + " no longer exists.");
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
            Utilisateur utilisateur;
            try {
                utilisateur = em.getReference(Utilisateur.class, id);
                utilisateur.getIdUtilisateur();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The utilisateur with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Mission> missionListOrphanCheck = utilisateur.getMissionList();
            for (Mission missionListOrphanCheckMission : missionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Utilisateur (" + utilisateur + ") cannot be destroyed since the Mission " + missionListOrphanCheckMission + " in its missionList field has a non-nullable demandeurId field.");
            }
            List<Mission> missionList1OrphanCheck = utilisateur.getMissionList1();
            for (Mission missionList1OrphanCheckMission : missionList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Utilisateur (" + utilisateur + ") cannot be destroyed since the Mission " + missionList1OrphanCheckMission + " in its missionList1 field has a non-nullable intervenantId field.");
            }
            List<Intervention> interventionListOrphanCheck = utilisateur.getInterventionList();
            for (Intervention interventionListOrphanCheckIntervention : interventionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Utilisateur (" + utilisateur + ") cannot be destroyed since the Intervention " + interventionListOrphanCheckIntervention + " in its interventionList field has a non-nullable intervenantId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TypeUtilisateur utilisateurType = utilisateur.getUtilisateurType();
            if (utilisateurType != null) {
                utilisateurType.getUtilisateurList().remove(utilisateur);
                utilisateurType = em.merge(utilisateurType);
            }
            em.remove(utilisateur);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Utilisateur> findUtilisateurEntities() {
        return findUtilisateurEntities(true, -1, -1);
    }

    public List<Utilisateur> findUtilisateurEntities(int maxResults, int firstResult) {
        return findUtilisateurEntities(false, maxResults, firstResult);
    }

    private List<Utilisateur> findUtilisateurEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Utilisateur.class));
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

    public Utilisateur findUtilisateur(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Utilisateur.class, id);
        } finally {
            em.close();
        }
    }

    public int getUtilisateurCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Utilisateur> rt = cq.from(Utilisateur.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
