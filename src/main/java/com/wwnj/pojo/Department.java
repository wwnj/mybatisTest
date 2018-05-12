package com.wwnj.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wb on 2018/5/7.
 */
public class Department implements Serializable {

    private Integer id;
    private String deptName;
    private List<Employee> employees;

    public Department() {
    }

    public Department(Integer id) {
        this.id = id;
    }

    public Department(Integer id, String deptName) {
        this.id = id;
        this.deptName = deptName;
    }
    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", deptName='" + deptName + '\'' +
                '}';
    }
}
