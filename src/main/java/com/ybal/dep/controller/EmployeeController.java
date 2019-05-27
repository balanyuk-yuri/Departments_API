package com.ybal.dep.controller;

import com.ybal.dep.model.Certificate;
import com.ybal.dep.model.Employee;
import com.ybal.dep.service.EmployeeService;
import net.minidev.json.JSONObject;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@RestController
public class EmployeeController extends BaseController{
    private final static String empPrefix = "/emp";
    private final static String certPrefix = "/cert";
    @Autowired
    private EmployeeService employeeService;

    @GetMapping(empPrefix + "/{id}")
    public Employee employee(@PathVariable("id") Long depId){
        return employeeService.employee(depId);
    }

    @GetMapping(empPrefix)
    public List<Employee> employeesOf(@RequestParam(name = "depId") Long depId){
        return employeeService.employeesOf(depId);
    }

    @PostMapping(empPrefix)
    public Employee addNewEmployee(@RequestBody Employee emp){
        return employeeService.addNewEmployee(emp);
    }

    @PutMapping(empPrefix + "/{id}")
    public Employee updateEmployee(@PathVariable("id") Long empId,
                                   @RequestBody Employee employee){
        return employeeService.updateEmployee(empId,
                employee.getDepartment() != null ? employee.getDepartment().getId() : null,
                employee.getGrade() != null ? employee.getGrade().getId() : null,
                employee.getPost() != null ? employee.getPost().getId() : null,
                employee.getSalary());
    }

    @PutMapping(empPrefix + "/{id}/addCert")
    public Certificate addEmployeeCertificate(@PathVariable("id") Long empId,
                                              @RequestPart Certificate certificate,
                                              @RequestParam("certScan") MultipartFile scanFile) throws IOException {
        certificate.setImage(scanFile.getBytes());
        return employeeService.addEmployeeCertificate(empId, certificate);
    }

    @GetMapping(certPrefix + "/{id}")
    public void certificateImage(@PathVariable("id") Long id ,
                                 HttpServletResponse response) throws IOException {
        InputStream in = new ByteArrayInputStream(employeeService.certificateImage(id));
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    @DeleteMapping(empPrefix)
    public boolean freeEmployee(@RequestParam(value = "empId") Long empId){
        return employeeService.freeEmployee(empId);
    }

    @GetMapping(empPrefix + "/history")
    public List<JSONObject> employeeHistory(@RequestParam(value = "id", required = false) Long id,
                                            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern="ddMMyyyy") Date from,
                                            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern="ddMMyyyy") Date to,
                                            @RequestParam(value = "ip", required = false) String ip){
        return history(Employee.class, id, from, to, ip);
    }
}
