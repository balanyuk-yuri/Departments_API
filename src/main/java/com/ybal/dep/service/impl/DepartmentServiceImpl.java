package com.ybal.dep.service.impl;

import com.ybal.dep.model.*;
import com.ybal.dep.repo.*;
import com.ybal.dep.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public Department department(Long id){
        return  departmentRepository.findById(id).get();
    }

    @Override
    public List<Department> departmentsOf(Long parentId) {
        return departmentRepository.findDepartmentsByParentId(parentId);
    }

    @Override
    public Department createDepartment(Department dep) {
        return departmentRepository.save(dep);
    }

    @Override
    public Department updateDepartment(Long depId, Long parentId, Long chiefId) {
        Optional<Department> depOpt = departmentRepository.findById(depId);
        if (!depOpt.isPresent()) {
            return null;
        }

        Department dep = depOpt.get();
        if (parentId != null && departmentRepository.existsById(parentId)){
            dep.setParent(departmentRepository.findById(parentId).get());
        }

        if (chiefId != null && employeeRepository.existsById(chiefId)){
            dep.setChief(employeeRepository.findById(chiefId).get());
        }
        return departmentRepository.save(dep);
    }

    @Override
    public boolean closeDepartment(Long depId) {
        Optional<Department> depOpt = departmentRepository.findById(depId);
        if (!depOpt.isPresent()) {
            return false;
        }
        Department dep = depOpt.get();
        if (employeeRepository.existsByDepartmentId(depId)) {
            return false;
        }

        departmentRepository.delete(dep);
        return true;
    }
}
