/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.manager;

import com.lsmp.model.Infrastructure;
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
@Repository("InfrastructureDao")
@Transactional
public class InfrastructureDaoImpl implements InfrastructureDao {

    @Autowired
    SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Override
    public Infrastructure findByUserName(String username) {
        Session session = null;
        List<Infrastructure> infrastructures = new ArrayList<Infrastructure>();
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            infrastructures = session.createQuery("from Infrastructure where identifiant_Infrastructure=?")
                    .setParameter(0, username).list();
            session.getTransaction().commit();

            if (infrastructures.size() > 0) {
                return infrastructures.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<Infrastructure> findAllInfrastructures() {
        Session session = null;
        List<Infrastructure> userList = null;
        try {
            session = sessionFactory.openSession();
            userList = session.createQuery("from Infrastructure").list();
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
    public void saveInfrastructure(Infrastructure newInfrastructure) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(newInfrastructure);
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
