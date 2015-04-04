/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.manager;

import com.lsmp.model.Utilisateur;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TiDy
 */
public class UtilisateurManager extends GeneralManager {

    @SuppressWarnings("unchecked")
    public Utilisateur findByUserName(String username) {

        List<Utilisateur> users = new ArrayList<Utilisateur>();
        try {
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

    public List<Utilisateur> findAllUsers() {
        List<Utilisateur> result = null;
        try {
            session.beginTransaction();
            result = session.createQuery("from Utilisateur").list();
            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void saveUtilisateur(Utilisateur newUtilisateur) {
        try {
            session.beginTransaction();
            session.persist(newUtilisateur);
            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
