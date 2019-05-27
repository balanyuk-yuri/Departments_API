package com.ybal.dep.repo;

import com.ybal.dep.model.Department;
import com.ybal.dep.model.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    boolean existsByDepartmentId(Long depId);
    List<Employee> findByDepartmentId(Long depId);
}
