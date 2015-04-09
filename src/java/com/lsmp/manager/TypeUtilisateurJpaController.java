/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.manager;

import com.lsmp.manager.exceptions.IllegalOrphanException;
import com.lsmp.manager.exceptions.NonexistentEntityException;
import com.lsmp.model.TypeUtilisateur;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.lsmp.model.Utilisateur;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TiDy
 */
public class TypeUtilisateurJpaController implements Serializable {

    public TypeUtilisateurJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TypeUtilisateur typeUtilisateur) {
        if (typeUtilisateur.getUtilisateurList() == null) {
            typeUtilisateur.setUtilisateurList(new ArrayList<Utilisateur>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Utilisateur> attachedUtilisateurList = new ArrayList<Utilisateur>();
            for (Utilisateur utilisateurListUtilisateurToAttach : typeUtilisateur.getUtilisateurList()) {
                utilisateurListUtilisateurToAttach = em.getReference(utilisateurListUtilisateurToAttach.getClass(), utilisateurListUtilisateurToAttach.getIdUtilisateur());
                attachedUtilisateurList.add(utilisateurListUtilisateurToAttach);
            }
            typeUtilisateur.setUtilisateurList(attachedUtilisateurList);
            em.persist(typeUtilisateur);
            for (Utilisateur utilisateurListUtilisateur : typeUtilisateur.getUtilisateurList()) {
                TypeUtilisateur oldUtilisateurTypeOfUtilisateurListUtilisateur = utilisateurListUtilisateur.getUtilisateurType();
                utilisateurListUtilisateur.setUtilisateurType(typeUtilisateur);
                utilisateurListUtilisateur = em.merge(utilisateurListUtilisateur);
                if (oldUtilisateurTypeOfUtilisateurListUtilisateur != null) {
                    oldUtilisateurTypeOfUtilisateurListUtilisateur.getUtilisateurList().remove(utilisateurListUtilisateur);
                    oldUtilisateurTypeOfUtilisateurListUtilisateur = em.merge(oldUtilisateurTypeOfUtilisateurListUtilisateur);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TypeUtilisateur typeUtilisateur) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TypeUtilisateur persistentTypeUtilisateur = em.find(TypeUtilisateur.class, typeUtilisateur.getIdTypeUtilisateur());
            List<Utilisateur> utilisateurListOld = persistentTypeUtilisateur.getUtilisateurList();
            List<Utilisateur> utilisateurListNew = typeUtilisateur.getUtilisateurList();
            List<String> illegalOrphanMessages = null;
            for (Utilisateur utilisateurListOldUtilisateur : utilisateurListOld) {
                if (!utilisateurListNew.contains(utilisateurListOldUtilisateur)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Utilisateur " + utilisateurListOldUtilisateur + " since its utilisateurType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Utilisateur> attachedUtilisateurListNew = new ArrayList<Utilisateur>();
            for (Utilisateur utilisateurListNewUtilisateurToAttach : utilisateurListNew) {
                utilisateurListNewUtilisateurToAttach = em.getReference(utilisateurListNewUtilisateurToAttach.getClass(), utilisateurListNewUtilisateurToAttach.getIdUtilisateur());
                attachedUtilisateurListNew.add(utilisateurListNewUtilisateurToAttach);
            }
            utilisateurListNew = attachedUtilisateurListNew;
            typeUtilisateur.setUtilisateurList(utilisateurListNew);
            typeUtilisateur = em.merge(typeUtilisateur);
            for (Utilisateur utilisateurListNewUtilisateur : utilisateurListNew) {
                if (!utilisateurListOld.contains(utilisateurListNewUtilisateur)) {
                    TypeUtilisateur oldUtilisateurTypeOfUtilisateurListNewUtilisateur = utilisateurListNewUtilisateur.getUtilisateurType();
                    utilisateurListNewUtilisateur.setUtilisateurType(typeUtilisateur);
                    utilisateurListNewUtilisateur = em.merge(utilisateurListNewUtilisateur);
                    if (oldUtilisateurTypeOfUtilisateurListNewUtilisateur != null && !oldUtilisateurTypeOfUtilisateurListNewUtilisateur.equals(typeUtilisateur)) {
                        oldUtilisateurTypeOfUtilisateurListNewUtilisateur.getUtilisateurList().remove(utilisateurListNewUtilisateur);
                        oldUtilisateurTypeOfUtilisateurListNewUtilisateur = em.merge(oldUtilisateurTypeOfUtilisateurListNewUtilisateur);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = typeUtilisateur.getIdTypeUtilisateur();
                if (findTypeUtilisateur(id) == null) {
                    throw new NonexistentEntityException("The typeUtilisateur with id " + id + " no longer exists.");
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
            TypeUtilisateur typeUtilisateur;
            try {
                typeUtilisateur = em.getReference(TypeUtilisateur.class, id);
                typeUtilisateur.getIdTypeUtilisateur();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The typeUtilisateur with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Utilisateur> utilisateurListOrphanCheck = typeUtilisateur.getUtilisateurList();
            for (Utilisateur utilisateurListOrphanCheckUtilisateur : utilisateurListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TypeUtilisateur (" + typeUtilisateur + ") cannot be destroyed since the Utilisateur " + utilisateurListOrphanCheckUtilisateur + " in its utilisateurList field has a non-nullable utilisateurType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(typeUtilisateur);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TypeUtilisateur> findTypeUtilisateurEntities() {
        return findTypeUtilisateurEntities(true, -1, -1);
    }

    public List<TypeUtilisateur> findTypeUtilisateurEntities(int maxResults, int firstResult) {
        return findTypeUtilisateurEntities(false, maxResults, firstResult);
    }

    private List<TypeUtilisateur> findTypeUtilisateurEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TypeUtilisateur.class));
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

    public TypeUtilisateur findTypeUtilisateur(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TypeUtilisateur.class, id);
        } finally {
            em.close();
        }
    }

    public int getTypeUtilisateurCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TypeUtilisateur> rt = cq.from(TypeUtilisateur.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
