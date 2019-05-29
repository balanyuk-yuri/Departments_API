package com.ybal.dep.repo.auth;

import com.ybal.dep.auth.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
