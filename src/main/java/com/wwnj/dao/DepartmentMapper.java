package com.wwnj.dao;

import com.wwnj.pojo.Department;

/**
 * Created by wb on 2018/5/7.
 */
public interface DepartmentMapper {

    public Department selectById(Integer id);

    public Department selectByIdWithEmployees(Integer id);
}
