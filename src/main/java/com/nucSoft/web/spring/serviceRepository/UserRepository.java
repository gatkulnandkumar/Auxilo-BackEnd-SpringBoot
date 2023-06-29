package com.nucSoft.web.spring.serviceRepository;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nucSoft.web.spring.model.User;

@Component
public class UserRepository {

	
	  @PersistenceContext
	    private EntityManager entityManager;

	    public Optional<User> findByUsername(String username) {
	        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
	        query.setParameter("username", username);
	        return query.getResultList().stream().findFirst();
	    }

	    public Boolean existsByUsername(String username) {
	        TypedQuery<Boolean> query = entityManager.createQuery("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username", Boolean.class);
	        query.setParameter("username", username);
	        return query.getSingleResult();
	    }

	    public Boolean existsByEmail(String email) {
	        TypedQuery<Boolean> query = entityManager.createQuery("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email", Boolean.class);
	        query.setParameter("email", email);
	        return query.getSingleResult();
	    }

	    public User findByEmail(String email) {
	        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
	        query.setParameter("email", email);
	        return query.getResultList().stream().findFirst().orElse(null);
	    }
}
