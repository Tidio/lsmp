/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmp.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
 
import com.lsmp.manager.UtilisateurDao;
import com.lsmp.model.TypeUtilisateur;
 
@Service("utilisateurService")
public class UtilisateurService implements UserDetailsService {
 
	//get user from the database, via Hibernate
	@Autowired
	private UtilisateurDao userDao;
 
	@Override
	public UserDetails loadUserByUsername(final String username) 
		throws UsernameNotFoundException {
 
		com.lsmp.model.Utilisateur user = userDao.findByUserName(username);
		List<GrantedAuthority> authorities = 
                                      buildUserAuthority(user.getTypeUtilisateur());
 
		return buildUserForAuthentication(user, authorities);
 
	}
 
	// Converts com.mkyong.users.model.User user to
	// org.springframework.security.core.userdetails.User
	private User buildUserForAuthentication(com.lsmp.model.Utilisateur user, 
		List<GrantedAuthority> authorities) {
		return new User(user.getIdentifiantUtilisateur(), user.getMdpUtilisateur(), 
			true, true, true, true, authorities);
	}
 
	private List<GrantedAuthority> buildUserAuthority(TypeUtilisateur userRoles) {
 
		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
 
		// Build user's authorities
		
			setAuths.add(new SimpleGrantedAuthority(userRoles.getRole()));
		
 
		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);
 
		return Result;
	}
 
}
