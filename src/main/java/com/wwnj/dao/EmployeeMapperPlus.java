package com.wwnj.dao;

import com.wwnj.pojo.Employee;

/**
 * Created by wb on 2018/5/7.
 */
public interface EmployeeMapperPlus {

    public Employee selectByIdReturnWithDept(Integer id);

    public Employee selectByIdReturnByStep(Integer id);

    public Employee selectByIdReturnByStepAndDiscriminator(Integer id);
}
