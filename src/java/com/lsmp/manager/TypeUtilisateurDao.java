/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.manager;

import com.lsmp.model.TypeUtilisateur;
import java.util.List;

/**
 *
 * @author TiDy
 */
public interface TypeUtilisateurDao {
    
    List<TypeUtilisateur> findAllTypeUtilisateur();
    
    TypeUtilisateur find(int id);
}
