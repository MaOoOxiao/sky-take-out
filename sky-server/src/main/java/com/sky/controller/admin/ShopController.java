package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("AdminShopController")
@Slf4j
@Api(tags = "店铺接口")
@RequestMapping("/admin/shop")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;
    private static final String KEY = "SHOP_STATUS";

    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result setStatus(@PathVariable Integer status){
        redisTemplate.opsForValue().set( KEY , status );
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        return Result.success(status);
    }



}
