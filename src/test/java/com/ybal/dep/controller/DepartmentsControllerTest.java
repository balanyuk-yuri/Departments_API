package com.ybal.dep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ybal.dep.DepApplication;
import com.ybal.dep.model.Department;
import com.ybal.dep.model.Employee;
import com.ybal.dep.service.DepartmentService;
import org.hibernate.mapping.Array;
import org.hibernate.mapping.Collection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestBody;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DepApplication.class)
@WebAppConfiguration
public class DepartmentsControllerTest {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private DepartmentService departmentServiceMock;
    Department dep1 = new Department();
    Department dep2 = new Department();
    Employee emp1 = new Employee();
    Employee emp2 = new Employee();

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();

        dep1.setId(1L);
        dep1.setName("firstDep");
        dep1.setChief(emp1);
        dep1.getEmployees().add(emp1);

        dep2.setId(2L);
        dep2.setName("secondDep");
        dep2.setChief(emp2);
        dep2.getEmployees().add(emp2);
        dep2.setParent(dep1);

        emp1.setId(1L);
        emp2.setId(2L);
    }

    @Test
    public void getDepartmentTest() throws Exception {
        when(departmentServiceMock.department(dep1.getId())).thenReturn(dep1);
        mockMvc.perform(get("/dep/{id}", dep1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andDo(document("get-department-example",
                        pathParameters(
                                parameterWithName("id").description("The id of the department to get")
                        )));
    }

    @Test
    public void getDepartmentOfTest() throws Exception {
        when(departmentServiceMock.departmentsOf(dep1.getId())).thenReturn(Arrays.asList(dep2));
        mockMvc.perform(get("/dep?parentId={id}", dep1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(dep2.getId().intValue())))
                .andDo(document("get-departments-within-parent-example",
                        requestParameters(
                                parameterWithName("parentId").description("The id of the parent department")
                        )));
    }

    @Test
    public void createDepartmentTest() throws Exception {
        when(departmentServiceMock.createDepartment(dep1)).thenReturn(dep1);
        dep1.setChief(null);
        String inJson = objectMapper.writeValueAsString(dep1);
        mockMvc.perform(post("/dep").contentType(MediaType.APPLICATION_JSON_VALUE).content(inJson))
                .andExpect(status().isOk())
                .andDo(document("create-department-example"));
    }

    @Test
    public void updateDepartmentTest() throws Exception {
        String inJson = "{\"chief\":{\"id\":2}}";
        when(departmentServiceMock.updateDepartment(dep1.getId(), null, emp2.getId())).thenReturn(dep1);
        mockMvc.perform(put("/dep/{id}", dep1.getId()).contentType(MediaType.APPLICATION_JSON_VALUE).content(inJson))
                .andExpect(status().isOk())
                .andDo(document("update-department-example",
                        pathParameters(parameterWithName("id").description("The id of the department to update"))));
    }

    @Test
    public void closeDepartmentTest() throws Exception {
        when(departmentServiceMock.closeDepartment(dep2.getId())).thenReturn(true);
        dep2.getEmployees().clear();
        mockMvc.perform(delete("/dep/{id}", dep2.getId()))
                .andExpect(status().isOk())
                .andDo(document("close-department-example",
                        pathParameters(parameterWithName("id").description("The id of the department to close"))));
    }
}


