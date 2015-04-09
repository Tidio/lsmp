/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.manager;

import com.lsmp.model.Utilisateur;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TiDy
 */
@Repository("UtilisateurDao")
@Transactional
public class UtilisateurDaoImpl implements UtilisateurDao {

    @Autowired
    SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Override
    public Utilisateur findByUserName(String username) {
        Session session = null;
        List<Utilisateur> users = new ArrayList<Utilisateur>();
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            users = session.createQuery("from Utilisateur where identifiant_utilisateur=?")
                    .setParameter(0, username).list();
            session.getTransaction().commit();

            if (users.size() > 0) {
                return users.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<Utilisateur> findAllUsers() {
        Session session = null;
        List<Utilisateur> userList = null;
        try {
            session = sessionFactory.openSession();
            userList = session.createQuery("from Utilisateur").list();
        } catch (Exception e) {
            //Logging
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
                session = null;
            }
        }
        
        return userList;
    }

    @Override
    public void saveUtilisateur(Utilisateur newUtilisateur) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(newUtilisateur);
            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (session != null && session.isOpen()) {
                session.close();
                session = null;
            }
        }
    }

}
