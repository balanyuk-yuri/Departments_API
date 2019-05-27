package com.ybal.dep.repo;

import com.ybal.dep.model.Department;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
    List<Department> findDepartmentsByParentId(Long parentId);

    @Cacheable("departments")
    Optional<Department> findById(Long id);

    @Override
    @CacheEvict("departments")
    <D extends Department> D save(D department);

    @Override
    @CacheEvict("departments")
    void delete(Department department);
}
