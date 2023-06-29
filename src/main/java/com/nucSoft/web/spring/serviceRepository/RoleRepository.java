package com.nucSoft.web.spring.serviceRepository;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nucSoft.web.spring.model.ERole;
import com.nucSoft.web.spring.model.Role;

@Component
public class RoleRepository {

	@PersistenceContext
    private EntityManager entityManager;

    public Optional<Role> findByName(ERole name) {
        TypedQuery<Role> query = entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findFirst();
    }
}
