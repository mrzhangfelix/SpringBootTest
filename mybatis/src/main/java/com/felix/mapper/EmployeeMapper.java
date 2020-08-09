package com.felix.mapper;

import com.felix.bean.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

//或者@MapperScan将接口扫描装配到容器中
@Repository
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public void insertEmp(Employee employee);
}
