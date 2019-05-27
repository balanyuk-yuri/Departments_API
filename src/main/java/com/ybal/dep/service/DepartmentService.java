package com.ybal.dep.service;

import com.ybal.dep.model.*;

import java.util.*;

public interface DepartmentService {
    Department department(Long id);
    List<Department> departmentsOf(Long parentId);
    Department createDepartment(Department dep);
    Department updateDepartment(Long depId, Long parentId, Long chiefId);
    boolean closeDepartment(Long depId);
}
