package com.wwnj.mapper;

import com.wwnj.dao.EmployeeMapper;
import com.wwnj.pojo.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wb on 2018/5/10.
 */
public class CacheTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException{
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    /**
     * 两级缓存
     * 一级缓存：(本地缓存)：SqlSession级别的缓存。一级缓存是一直开启的；实际上是SqlSession级别的一个map
     *      与数据库同一次会话期间查询到的数据会放在本地缓存中。
     *      以后如果需要获取相同的数据，直接从缓存中拿，没必要再去查询数据库
     *
     *      一级缓存失效情况（没有使用到当前一级缓存的情况，效果就是，还需要再向数据库发出查询）
     *          1.SqlSession不同
     *          2.SqlSession相同，查询条件不同（当前一级缓存中还没有这个数据）
     *          3.SqlSession相同，两次查询之间执行了增删改操作（这次增删改可能对当前数据有影响）
     *          4.SqlSession相同，手动清除了一级缓存（缓存清空）
     * 二级缓存：(全局缓存)：基于namespace级别的缓存；一个namespace对应一个二级缓存
     *      工作机制：
     *      1.一个会话，查询一条数据，这个数据就会被放在当前会话的一级缓存中；
     *      2.如果会话关闭；一级缓存中的数据会被保存到二级缓存中；新的会话查询信息，就可以参照二级缓存中的内容；
     *      3.SqlSession===EmployeeMapper====>Employee
     *                  ===DepartmentMapper=====>Department
     *                  不同namespace查出的数据会放在自己对应的缓存（map）中
     *                  效果：数据会从二级缓存中获取
     *                      查出的数据都会被默认先放在一级缓存中
     *                      只有会话提交或者关闭后，一级缓存中的数据才会转移到二级缓存中
     *      使用：
     *          1.开启全局二级缓存配置
     *          2.去mapper.xml中配置使用二级缓存
     *          3.我们的POJO需要实现序列化接口
     *
     * 和缓存有关的设置/属性：
     *          1.cacheEnabled=true（默认）：false关闭缓存（二级缓存关闭）（一级缓存一直可用）
     *          2.每个select标签都有useCache="true"（默认），
     *                                  false不使用缓存（一级缓存依然使用）（不使用二级缓存）
     *          3.每个增删改标签：flushCache="true"（默认），增删改执行完成后一级二级缓存都会清除缓存
     *                          测试：flushCache="true"：一级缓存就清空了;二级缓存也会被清空；
     *                          查询标签：flushCache="false"（默认）
     *                              如果flushCache="true"；每次查询之后都会清空缓存；缓存是没有被使用的；
     *          4.sqlSession.clearSession()；只是清除当前sqlSession的一级缓存
     *          5.localCacheScope：本地缓存作用域（一级缓存SESSION），当前会话的所有数据保存在会话缓存中
     *                           STATEMENT可以禁用掉一级缓存
     *
     * 第三方缓存整合：
     *          1.导入第三方缓存包即可
     *          2.导入与第三方缓存整合的适配包；官方有
     *          3.mapper.xml中使用自定义缓存
     */
    @Test
    public void testFirstLevelCache() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee emp01 = mapper.selectById(1);
            System.out.println(emp01);
            Employee emp02 = mapper.selectById(1);
            System.out.println(emp02);
            System.out.println(emp01==emp02);
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSecondLevelCache() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        try{
            EmployeeMapper mapper1 = sqlSession1.getMapper(EmployeeMapper.class);
            EmployeeMapper mapper2 = sqlSession2.getMapper(EmployeeMapper.class);

            Employee employee1 = mapper1.selectById(1);
            System.out.println(employee1.hashCode());
            sqlSession1.close();

            Employee employee2 = mapper2.selectById(1);
            System.out.println(employee2.hashCode());
            System.out.println(employee1==employee2);
        }finally {

            sqlSession2.close();
        }
    }
}
