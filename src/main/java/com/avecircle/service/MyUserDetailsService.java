package com.avecircle.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.avecircle.entity.UserEntity;
import com.avecircle.repo.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepo repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity entity = repo.findByUname(username);
		
		return new User(entity.getUname(),entity.getUpwd(),Collections.emptyList());
	}
	
	public boolean saveUser(UserEntity user)
	{
		user = repo.save(user);
		return user.getuId()!=null;
	}

}
