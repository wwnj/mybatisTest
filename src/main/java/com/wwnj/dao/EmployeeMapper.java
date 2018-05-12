package com.wwnj.dao;

import com.wwnj.pojo.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by wb on 2018/5/6.
 */
public interface EmployeeMapper {

    public List<Employee> selectByDid(Integer did);

    public Employee selectById(Integer id);

    public boolean insertByEmp(Employee employee);

    public boolean updateByEmp(Employee employee);

    public boolean deleteById(Integer id);

    public Employee selectByLaseNameAndGender(@Param("lastName") String lastName, @Param("gender") String gender);

    public Employee selectByMap(Map<String,Object> map);

    public List<Employee> selectByLike(String lastName);

    @MapKey("id")
    public Map<Integer,Employee> selectByLikeReturnMap(String lastName);

}
