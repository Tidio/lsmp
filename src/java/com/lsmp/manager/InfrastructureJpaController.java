/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.manager;

import com.lsmp.manager.exceptions.NonexistentEntityException;
import com.lsmp.model.Infrastructure;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.lsmp.model.Mission;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TiDy
 */
public class InfrastructureJpaController implements Serializable {

    public InfrastructureJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Infrastructure infrastructure) {
        if (infrastructure.getMissionList() == null) {
            infrastructure.setMissionList(new ArrayList<Mission>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Mission> attachedMissionList = new ArrayList<Mission>();
            for (Mission missionListMissionToAttach : infrastructure.getMissionList()) {
                missionListMissionToAttach = em.getReference(missionListMissionToAttach.getClass(), missionListMissionToAttach.getIdMission());
                attachedMissionList.add(missionListMissionToAttach);
            }
            infrastructure.setMissionList(attachedMissionList);
            em.persist(infrastructure);
            for (Mission missionListMission : infrastructure.getMissionList()) {
                missionListMission.getInfrastructureList().add(infrastructure);
                missionListMission = em.merge(missionListMission);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Infrastructure infrastructure) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Infrastructure persistentInfrastructure = em.find(Infrastructure.class, infrastructure.getIdInfrastructure());
            List<Mission> missionListOld = persistentInfrastructure.getMissionList();
            List<Mission> missionListNew = infrastructure.getMissionList();
            List<Mission> attachedMissionListNew = new ArrayList<Mission>();
            for (Mission missionListNewMissionToAttach : missionListNew) {
                missionListNewMissionToAttach = em.getReference(missionListNewMissionToAttach.getClass(), missionListNewMissionToAttach.getIdMission());
                attachedMissionListNew.add(missionListNewMissionToAttach);
            }
            missionListNew = attachedMissionListNew;
            infrastructure.setMissionList(missionListNew);
            infrastructure = em.merge(infrastructure);
            for (Mission missionListOldMission : missionListOld) {
                if (!missionListNew.contains(missionListOldMission)) {
                    missionListOldMission.getInfrastructureList().remove(infrastructure);
                    missionListOldMission = em.merge(missionListOldMission);
                }
            }
            for (Mission missionListNewMission : missionListNew) {
                if (!missionListOld.contains(missionListNewMission)) {
                    missionListNewMission.getInfrastructureList().add(infrastructure);
                    missionListNewMission = em.merge(missionListNewMission);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = infrastructure.getIdInfrastructure();
                if (findInfrastructure(id) == null) {
                    throw new NonexistentEntityException("The infrastructure with id " + id + " no longer exists.");
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
            Infrastructure infrastructure;
            try {
                infrastructure = em.getReference(Infrastructure.class, id);
                infrastructure.getIdInfrastructure();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The infrastructure with id " + id + " no longer exists.", enfe);
            }
            List<Mission> missionList = infrastructure.getMissionList();
            for (Mission missionListMission : missionList) {
                missionListMission.getInfrastructureList().remove(infrastructure);
                missionListMission = em.merge(missionListMission);
            }
            em.remove(infrastructure);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Infrastructure> findInfrastructureEntities() {
        return findInfrastructureEntities(true, -1, -1);
    }

    public List<Infrastructure> findInfrastructureEntities(int maxResults, int firstResult) {
        return findInfrastructureEntities(false, maxResults, firstResult);
    }

    private List<Infrastructure> findInfrastructureEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Infrastructure.class));
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

    public Infrastructure findInfrastructure(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Infrastructure.class, id);
        } finally {
            em.close();
        }
    }

    public int getInfrastructureCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Infrastructure> rt = cq.from(Infrastructure.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
