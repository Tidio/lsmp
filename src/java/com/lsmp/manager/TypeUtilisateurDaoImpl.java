/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.manager;

import com.lsmp.model.TypeUtilisateur;
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
@Repository("TypeUtilisateurDao")
@Transactional
public class TypeUtilisateurDaoImpl implements TypeUtilisateurDao{

    @Autowired
    SessionFactory sessionFactory;

    Session session = null;

    @Override
    public List<TypeUtilisateur> findAllTypeUtilisateur() {
        List<TypeUtilisateur> result = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            result = session.createQuery("from TypeUtilisateur").list();
            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
                session = null;
            }
        }

        return result;
    }
    
    @Override
    public TypeUtilisateur find(int id){
        TypeUtilisateur result = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            result = (TypeUtilisateur)session.load(TypeUtilisateur.class, id);
            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
                session = null;
            }
        }
        
        return result;
    }

}
