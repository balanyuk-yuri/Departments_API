package com.ybal.dep.service;

import com.ybal.dep.model.Certificate;
import com.ybal.dep.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee employee(Long id);
    List<Employee> employeesOf(Long depId);
    Employee addNewEmployee(Employee emp);
    Employee updateEmployee(Long empId, Long depId, Long postId, Long gradeId, Integer salary);
    Certificate addEmployeeCertificate(Long empId, Certificate certificate);
    byte[] certificateImage(Long certId);
    boolean freeEmployee(Long empId);
}
