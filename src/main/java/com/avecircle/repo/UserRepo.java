package com.avecircle.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avecircle.entity.UserEntity;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Integer>{
	
	
	// select * from sbi_security where uname=:uname;	
	 public UserEntity findByUname(String uname);
	
	

}
