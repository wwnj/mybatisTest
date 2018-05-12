package com.wwnj.mapper;

import com.wwnj.dao.DepartmentMapper;
import com.wwnj.dao.EmployeeMapper;
import com.wwnj.dao.EmployeeMapperDynamicSQL;
import com.wwnj.dao.EmployeeMapperPlus;
import com.wwnj.pojo.Department;
import com.wwnj.pojo.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by wb on 2018/5/6.
 */
public class MapperTest {

    /**
     * SqlSessionFactory同Connection一样都是非线程安全的
     * @return
     */
    private SqlSessionFactory getSqlSessionFactory() {
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            System.out.println("IoException + " + e.getMessage());;
        }
        SqlSessionFactory sqlSessionFactory =
                new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;
    }

    @Test
    public void test01(){
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Employee employee = session.selectOne(
                    "com.wwnj.mapper.EmployeeMapper.selectById", 1);
            System.out.println(employee);
        } finally {
            session.close();
        }
    }

    @Test
    public void test02(){
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Employee employee = mapper.selectById(1);
            System.out.println(employee);
        } finally {
            session.close();
        }
    }

    @Test
    public void test03(){
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Employee james = new Employee(null, "hill", "hill@wwnj.com", "0");
            boolean successful = mapper.insertByEmp(james);
            System.out.println(successful);

            Employee employee = mapper.selectById(james.getId());
            System.out.println(employee);

            session.commit();

           /* Employee wade = new Employee(7, "wade", "wade@wwnj.com", "1");
            boolean successful = mapper.updateByEmp(wade);
            session.commit();
            System.out.println(successful);
            Employee employee = mapper.selectById(wade.getId());
            System.out.println(employee);*/

            /*boolean successful = mapper.deleteById(7);
            session.commit();
            System.out.println(successful);*/
        } finally {
            session.close();
        }
    }

    @Test
    public void test04(){
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            //Employee employee = mapper.selectByLaseNameAndGender("hill","0");
            /*Map<String,Object> map = new HashMap<>();
            map.put("lastName","hill");
            map.put("gender","1");
            Employee employee = mapper.selectByMap(map);
            System.out.println(employee);*/

            /*List<Employee> employees = mapper.selectByLike("%i%");
            System.out.println(employees);*/
            Map<Integer, Employee> employeeMap = mapper.selectByLikeReturnMap("%i%");
            System.out.println(employeeMap);
        } finally {
            session.close();
        }
    }

    @Test
    public void test05(){
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            /*EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);
            Employee employee = mapper.selectByIdReturnWithDept(1);
            System.out.println(employee);*/
            EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);
            Employee employee = mapper.selectByIdReturnByStep(1);
            System.out.println(employee.getLastName());
        }finally {
            openSession.close();
        }
    }

    @Test
    public void test06(){
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);
            Department department = mapper.selectByIdWithEmployees(1);
            System.out.println(department.getDeptName());
            /*List<Employee> employees = department.getEmployees();
            for (Employee emp:employees
                 ) {
                System.out.println(emp);
            }*/
        }finally {
            openSession.close();
        }
    }

    @Test
    public void test07(){
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);
            Employee employee = mapper.selectByIdReturnByStepAndDiscriminator(1);
            System.out.println(employee);
            employee = mapper.selectByIdReturnByStepAndDiscriminator(9);
            System.out.println(employee);
        }finally {
            openSession.close();
        }
    }

    @Test
    public void test08(){
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            //List<Employee> employees = mapper.getEmpsByConditionIf(new Employee(null,null,null,"1"));
            //List<Employee> employees = mapper.getEmpsByConditionTrim(new Employee(null,null,null,"1"));
            //List<Employee> employees = mapper.getEmpsByConditionChoose(new Employee(1,null,null,"1"));
            //System.out.println(employees);

            /*Employee updateEmp = new Employee(8,null,"update@wwnj.com",null);
            mapper.updateEmp(updateEmp);
            openSession.commit();
            List<Employee> employees = mapper.getEmpsByConditionIf(updateEmp);
            System.out.println(employees);*/

            List<Employee> employees = mapper.getEmpsByConditionForeach(Arrays.asList(1,8,9));
            System.out.println(employees);
        }finally {
            openSession.close();
        }
    }

    @Test
    public void test09(){
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperDynamicSQL mapper = sqlSession.getMapper(EmployeeMapperDynamicSQL.class);
            List<Employee> employees = new ArrayList<>();
            employees.add(new Employee(null,"stevens","stevens@wwnj.com","1",new Department(1)));
            employees.add(new Employee(null,"beyonce","beyonce@wwnj.com","0",new Department(2)));
            mapper.addEmps(employees);
            sqlSession.commit();
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void test10(){
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapperDynamicSQL mapper = sqlSession.getMapper(EmployeeMapperDynamicSQL.class);
            Employee employee = new Employee();
            employee.setLastName("e");
            List<Employee> employees = mapper.getEmpsInnerParameterAndBind(employee);
            for (Employee emp:employees) {
                System.out.println(emp);
            }
        }finally {
            sqlSession.close();
        }
    }
}
