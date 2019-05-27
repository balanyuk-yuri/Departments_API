package com.ybal.dep.service.impl;

import com.ybal.dep.model.Certificate;
import com.ybal.dep.model.Department;
import com.ybal.dep.model.Employee;
import com.ybal.dep.repo.*;
import com.ybal.dep.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Employee employee(Long id){
        return employeeRepository.findById(id).get();
    }

    @Override
    public List<Employee> employeesOf(Long depId) {
        Optional<Department> depOpt = departmentRepository.findById(depId);
        if (!depOpt.isPresent()) {
            return new ArrayList<>();
        }
        return employeeRepository.findByDepartmentId(depId);
    }

    @Override
    public Employee addNewEmployee(Employee emp){
        return employeeRepository.save(emp);
    }

    @Override
    public Employee updateEmployee(Long empId, Long depId, Long postId, Long gradeId, Integer salary) {
        if (!employeeRepository.existsById(empId)) {
            return null;
        }

        Employee emp = employeeRepository.findById(empId).get();

        if (depId != null && departmentRepository.existsById(depId)){
            emp.setDepartment(departmentRepository.findById(depId).get());
        }

        if (postId != null && postRepository.existsById(postId)){
            emp.setPost(postRepository.findById(postId).get());
        }

        if (gradeId != null && gradeRepository.existsById(gradeId)){
            emp.setGrade(gradeRepository.findById(gradeId).get());
        }

        if (salary != null){
            emp.setSalary(salary);
        }

        return employeeRepository.save(emp);
    }

    @Override
    public Certificate addEmployeeCertificate(Long empId, Certificate certificate){
        if (!employeeRepository.existsById(empId)) {
            return null;
        }

        Employee emp = employeeRepository.findById(empId).get();
        emp.getCertificates().add(certificate);
        certificate.setEmployee(emp);
        employeeRepository.save(emp);
        return certificateRepository.save(certificate);
    }

    @Override
    public byte[] certificateImage(Long certId){
        if (!certificateRepository.existsById(certId)){
            return new byte[0];
        }
        return certificateRepository.findById(certId).get().getImage();
    }

    @Override
    public boolean freeEmployee(Long empId) {
        if (!employeeRepository.existsById(empId)) {
            return false;
        }
        employeeRepository.deleteById(empId);
        return true;
    }
}
