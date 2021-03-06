package com.ybal.dep.controller;

import com.ybal.dep.model.Department;
import com.ybal.dep.service.DepartmentService;
import net.minidev.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class DepartmentsController extends BaseController {
    @Autowired
    private DepartmentService departmentService;
    private final static String depPrefix = "/dep";

    @GetMapping({"/guest" + depPrefix + "/{id}", depPrefix + "/{id}"})
    public Department department(@PathVariable Long id) {
        return departmentService.department(id);
    }

    @GetMapping({"/guest" + depPrefix, depPrefix})
    public List<Department> departmentsOf(@RequestParam(value="parentId", required = false)Long parentId) {
        return departmentService.departmentsOf(parentId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEP_MANAGER')")
    @PostMapping(depPrefix)
    public Department createDepartment(@RequestBody Department department){
        return departmentService.createDepartment(department);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEP_MANAGER')")
    @PutMapping(depPrefix + "/{id}")
    public Department updateDepartment(@PathVariable("id") Long depId,
                                       @RequestBody Department department){
        return departmentService.updateDepartment(depId,
                department.getParent() != null ? department.getParent().getId() : null,
                department.getChief() != null ? department.getChief().getId() : null);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEP_MANAGER')")
    @DeleteMapping(depPrefix + "/{id}")
    public boolean closeDepartment(@PathVariable("id") Long depId){
        return departmentService.closeDepartment(depId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEP_MANAGER')")
    @GetMapping(depPrefix + "/history")
    public List<JSONObject> departmentHistory(@RequestParam(value = "id", required = false) Long id,
                                              @RequestParam(value = "from", required = false) @DateTimeFormat(pattern="ddMMyyyy")Date from,
                                              @RequestParam(value = "to", required = false) @DateTimeFormat(pattern="ddMMyyyy") Date to,
                                              @RequestParam(value = "ip", required = false) String ip){
        return history(Department.class, id, from, to, ip);
    }
}
