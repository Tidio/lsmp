/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.manager;

import com.lsmp.model.Infrastructure;
import java.util.List;

/**
 *
 * @author TiDy
 */
public interface InfrastructureDao {
    
    Infrastructure findByUserName(String username);
    
    List<Infrastructure> findAllInfrastructures();
    
    void saveInfrastructure(Infrastructure newInfrastructure);
    
}
