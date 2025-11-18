package com.sky.mapper;

import com.sky.dto.EmployeeDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 根据EmployeeDTO添加用户
     * 返回值: Boolean
     */
    @Insert("Insert Into employee(name,username ,password ,phone , sex ,id_number , status , create_time ,update_time , create_user ,update_user)"+
            " values( #{name}, #{name} , #{password} , #{phone} , #{sex} , #{idNumber} , #{status} , #{createTime} , #{updateTime} , #{createUser}, #{updateUser} )")
    Boolean addEmployee(Employee e);

    @Select("select count(*) from employee where name LIKE CONCAT('%', #{name}, '%') ")
    int getSizeByName(String name);

    List<Employee> getBynameIndexSize (@Param("name")String name );
}
