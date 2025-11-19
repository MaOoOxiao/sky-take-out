package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工接口")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation("登录方法 ")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 添加员工
     * @param employeeDTO
     * @return
     */
    @PostMapping
    public Result<Object> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        //查看当前线程id
        System.out.println("当前线程 ： "+Thread.currentThread().getId());
        Map<String, Object> data = new HashMap<>();
        data.put("success", employeeService.addEmployee(employeeDTO));
        return Result.<Object>success(MessageConstant.ADD_EMPLOYEE_SUCCESS,data);
    }

    /**
     * 员工分页查询 ， 模糊查询
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> pageQuery(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询 {}" ,employeePageQueryDTO );
        PageResult result = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(result);
    }

    /**
     * 修改员工账户状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("status/{status}")
    @ApiOperation("更改员工状态")
    public Result StartOrStop(@PathVariable Integer status , Long id){
        log.info("接受员工id和更改的状态 ： 状态为{} ，员工id为{}" , status , id  );
        employeeService.StartOrStop(status , id );
        return Result.success();
    }

    /**
     * 根据id查询员工并返回
     * @param id
     * @return Employee实体类
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id ){
        log.info("根据员工id={},来进行查询",id);
        return Result.success(employeeService.getById(id) );
    }

    /**
     * 修改员工信息
     * @param employeeDTO
     * @return
     */
    @ApiOperation("修改员工信息")
    @PutMapping
    public Result editEmployee(@RequestBody EmployeeDTO employeeDTO){
        employeeService.editEmployee(employeeDTO);
        return Result.success();
    }
}
