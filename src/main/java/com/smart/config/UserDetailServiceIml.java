package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.Userrepositery;
import com.smart.entities.User;

public class UserDetailServiceIml  implements UserDetailsService {

	@Autowired
	private Userrepositery userrepositery;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
	
		
		User user=userrepositery.getUserByUserName(username);
		if(user==null) {
			
			
			throw new UsernameNotFoundException("Could  Not Found User...");
		}
		
		//CustomUserDetails customUserDetails=new CustomUserDetails(user);
		
		CustomUserDetails customUserDetails=new CustomUserDetails(user);
		
		
		
		return customUserDetails;
	}

}
