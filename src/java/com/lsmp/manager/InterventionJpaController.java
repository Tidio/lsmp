/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.manager;

import com.lsmp.manager.exceptions.NonexistentEntityException;
import com.lsmp.model.Intervention;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.lsmp.model.Utilisateur;
import com.lsmp.model.Mission;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TiDy
 */
public class InterventionJpaController implements Serializable {

    public InterventionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Intervention intervention) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Utilisateur intervenantId = intervention.getIntervenantId();
            if (intervenantId != null) {
                intervenantId = em.getReference(intervenantId.getClass(), intervenantId.getIdUtilisateur());
                intervention.setIntervenantId(intervenantId);
            }
            Mission interventionMission = intervention.getInterventionMission();
            if (interventionMission != null) {
                interventionMission = em.getReference(interventionMission.getClass(), interventionMission.getIdMission());
                intervention.setInterventionMission(interventionMission);
            }
            em.persist(intervention);
            if (intervenantId != null) {
                intervenantId.getInterventionList().add(intervention);
                intervenantId = em.merge(intervenantId);
            }
            if (interventionMission != null) {
                interventionMission.getInterventionList().add(intervention);
                interventionMission = em.merge(interventionMission);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Intervention intervention) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Intervention persistentIntervention = em.find(Intervention.class, intervention.getIdIntervention());
            Utilisateur intervenantIdOld = persistentIntervention.getIntervenantId();
            Utilisateur intervenantIdNew = intervention.getIntervenantId();
            Mission interventionMissionOld = persistentIntervention.getInterventionMission();
            Mission interventionMissionNew = intervention.getInterventionMission();
            if (intervenantIdNew != null) {
                intervenantIdNew = em.getReference(intervenantIdNew.getClass(), intervenantIdNew.getIdUtilisateur());
                intervention.setIntervenantId(intervenantIdNew);
            }
            if (interventionMissionNew != null) {
                interventionMissionNew = em.getReference(interventionMissionNew.getClass(), interventionMissionNew.getIdMission());
                intervention.setInterventionMission(interventionMissionNew);
            }
            intervention = em.merge(intervention);
            if (intervenantIdOld != null && !intervenantIdOld.equals(intervenantIdNew)) {
                intervenantIdOld.getInterventionList().remove(intervention);
                intervenantIdOld = em.merge(intervenantIdOld);
            }
            if (intervenantIdNew != null && !intervenantIdNew.equals(intervenantIdOld)) {
                intervenantIdNew.getInterventionList().add(intervention);
                intervenantIdNew = em.merge(intervenantIdNew);
            }
            if (interventionMissionOld != null && !interventionMissionOld.equals(interventionMissionNew)) {
                interventionMissionOld.getInterventionList().remove(intervention);
                interventionMissionOld = em.merge(interventionMissionOld);
            }
            if (interventionMissionNew != null && !interventionMissionNew.equals(interventionMissionOld)) {
                interventionMissionNew.getInterventionList().add(intervention);
                interventionMissionNew = em.merge(interventionMissionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = intervention.getIdIntervention();
                if (findIntervention(id) == null) {
                    throw new NonexistentEntityException("The intervention with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Intervention intervention;
            try {
                intervention = em.getReference(Intervention.class, id);
                intervention.getIdIntervention();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The intervention with id " + id + " no longer exists.", enfe);
            }
            Utilisateur intervenantId = intervention.getIntervenantId();
            if (intervenantId != null) {
                intervenantId.getInterventionList().remove(intervention);
                intervenantId = em.merge(intervenantId);
            }
            Mission interventionMission = intervention.getInterventionMission();
            if (interventionMission != null) {
                interventionMission.getInterventionList().remove(intervention);
                interventionMission = em.merge(interventionMission);
            }
            em.remove(intervention);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Intervention> findInterventionEntities() {
        return findInterventionEntities(true, -1, -1);
    }

    public List<Intervention> findInterventionEntities(int maxResults, int firstResult) {
        return findInterventionEntities(false, maxResults, firstResult);
    }

    private List<Intervention> findInterventionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Intervention.class));
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

    public Intervention findIntervention(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Intervention.class, id);
        } finally {
            em.close();
        }
    }

    public int getInterventionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Intervention> rt = cq.from(Intervention.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
