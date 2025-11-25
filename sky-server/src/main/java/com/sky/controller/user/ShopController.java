package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("UserShopController")
@Slf4j
@Api(tags = "店铺接口")
@RequestMapping("/user/shop")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;
    private static final String KEY = "SHOP_STATUS";

    @GetMapping("/status")
    @ApiOperation("查询店铺营业状态")
    public Result getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        return Result.success(status);
    }



}
