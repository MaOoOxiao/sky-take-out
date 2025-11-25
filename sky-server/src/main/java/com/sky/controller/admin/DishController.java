package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags ="菜品接口")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("添加菜品")
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("添加菜品请求接受 ： {} " ,dishDTO);
        dishService.addDish(dishDTO);
        return Result.success("添加成功");
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result page(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询 {}" , dishPageQueryDTO);
        return Result.success(dishService.pageQuery(dishPageQueryDTO));
    }

    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteDish(@RequestParam List<Long> ids){
        log.info("批量删除菜品 {}" , ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("修改菜品状态")
    public Result updateStatus(@PathVariable Integer status,@RequestParam Long  id){
        log.info("修改菜品状态 {}， {}" ,id ,status);
        dishService.updateStatus(id , status);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("获取菜品信息")
    public Result<DishVO> getDishById(@PathVariable Long id){
        log.info("通过id获取菜品数据");
        return Result.success(dishService.getDishById(id));
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO){
        log.info("修改菜品请求接受 ： {} " ,dishDTO);
        dishService.updateDish(dishDTO);
        return Result.success("修改成功");
    }

}
