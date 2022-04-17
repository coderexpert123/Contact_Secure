package com.smart.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;


import java.util.List;

public interface ContactReositery extends JpaRepository<Contact, Integer>{
@Query("from Contact as c where c.user.id=:user_id")
public Page<Contact> findContactsByUser(@Param("user_id") int user_id,Pageable pageable);


}
