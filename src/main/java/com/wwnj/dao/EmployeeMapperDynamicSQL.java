package com.wwnj.dao;

import com.wwnj.pojo.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wb on 2018/5/10.
 */
public interface EmployeeMapperDynamicSQL {

    public List<Employee> getEmpsByConditionIf(Employee employee);

    public List<Employee> getEmpsByConditionTrim(Employee employee);

    public List<Employee> getEmpsByConditionChoose(Employee employee);

    public void updateEmp(Employee employee);

    public List<Employee> getEmpsByConditionForeach(List<Integer> ids);

    public void addEmps(@Param("employees") List<Employee> employees);

    public List<Employee> getEmpsInnerParameterAndBind(Employee employee);
}
