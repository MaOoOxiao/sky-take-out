package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j

public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    /**
     * 添加菜品
     * @param dishDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        log.info("添加 dish {}" , dish);
        dishMapper.insertDish(dish);
        log.info("添加 口味 {}" , dishDTO.getFlavors());
//        dishDTO.getFlavors().forEach(flavor -> {
//            flavor.setDishId(dish.getId());
//            dishMapper.insertFlavor( flavor ) ;
//        });
        dishDTO.getFlavors().forEach(flavorDTO -> {flavorDTO.setDishId(dish.getId());});
        dishMapper.insertBanch( dishDTO.getFlavors() ) ;
    }
}
