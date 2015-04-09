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
import com.lsmp.model.Mission;
import com.lsmp.model.TypeMission;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TiDy
 */
public class TypeMissionJpaController implements Serializable {

    public TypeMissionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TypeMission typeMission) {
        if (typeMission.getMissionList() == null) {
            typeMission.setMissionList(new ArrayList<Mission>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Mission> attachedMissionList = new ArrayList<Mission>();
            for (Mission missionListMissionToAttach : typeMission.getMissionList()) {
                missionListMissionToAttach = em.getReference(missionListMissionToAttach.getClass(), missionListMissionToAttach.getIdMission());
                attachedMissionList.add(missionListMissionToAttach);
            }
            typeMission.setMissionList(attachedMissionList);
            em.persist(typeMission);
            for (Mission missionListMission : typeMission.getMissionList()) {
                TypeMission oldMissionTypeOfMissionListMission = missionListMission.getMissionType();
                missionListMission.setMissionType(typeMission);
                missionListMission = em.merge(missionListMission);
                if (oldMissionTypeOfMissionListMission != null) {
                    oldMissionTypeOfMissionListMission.getMissionList().remove(missionListMission);
                    oldMissionTypeOfMissionListMission = em.merge(oldMissionTypeOfMissionListMission);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TypeMission typeMission) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TypeMission persistentTypeMission = em.find(TypeMission.class, typeMission.getIdTypeMission());
            List<Mission> missionListOld = persistentTypeMission.getMissionList();
            List<Mission> missionListNew = typeMission.getMissionList();
            List<String> illegalOrphanMessages = null;
            for (Mission missionListOldMission : missionListOld) {
                if (!missionListNew.contains(missionListOldMission)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Mission " + missionListOldMission + " since its missionType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Mission> attachedMissionListNew = new ArrayList<Mission>();
            for (Mission missionListNewMissionToAttach : missionListNew) {
                missionListNewMissionToAttach = em.getReference(missionListNewMissionToAttach.getClass(), missionListNewMissionToAttach.getIdMission());
                attachedMissionListNew.add(missionListNewMissionToAttach);
            }
            missionListNew = attachedMissionListNew;
            typeMission.setMissionList(missionListNew);
            typeMission = em.merge(typeMission);
            for (Mission missionListNewMission : missionListNew) {
                if (!missionListOld.contains(missionListNewMission)) {
                    TypeMission oldMissionTypeOfMissionListNewMission = missionListNewMission.getMissionType();
                    missionListNewMission.setMissionType(typeMission);
                    missionListNewMission = em.merge(missionListNewMission);
                    if (oldMissionTypeOfMissionListNewMission != null && !oldMissionTypeOfMissionListNewMission.equals(typeMission)) {
                        oldMissionTypeOfMissionListNewMission.getMissionList().remove(missionListNewMission);
                        oldMissionTypeOfMissionListNewMission = em.merge(oldMissionTypeOfMissionListNewMission);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = typeMission.getIdTypeMission();
                if (findTypeMission(id) == null) {
                    throw new NonexistentEntityException("The typeMission with id " + id + " no longer exists.");
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
            TypeMission typeMission;
            try {
                typeMission = em.getReference(TypeMission.class, id);
                typeMission.getIdTypeMission();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The typeMission with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Mission> missionListOrphanCheck = typeMission.getMissionList();
            for (Mission missionListOrphanCheckMission : missionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TypeMission (" + typeMission + ") cannot be destroyed since the Mission " + missionListOrphanCheckMission + " in its missionList field has a non-nullable missionType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(typeMission);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TypeMission> findTypeMissionEntities() {
        return findTypeMissionEntities(true, -1, -1);
    }

    public List<TypeMission> findTypeMissionEntities(int maxResults, int firstResult) {
        return findTypeMissionEntities(false, maxResults, firstResult);
    }

    private List<TypeMission> findTypeMissionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TypeMission.class));
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

    public TypeMission findTypeMission(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TypeMission.class, id);
        } finally {
            em.close();
        }
    }

    public int getTypeMissionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TypeMission> rt = cq.from(TypeMission.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
