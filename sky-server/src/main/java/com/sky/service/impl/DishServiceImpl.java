package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j

public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealDishMapper  setMealDishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
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

    @Override
    public PageResult pageQuery(DishPageQueryDTO dpq){
        log.info("service pageQuery方法启用 {}" , dpq);
        PageHelper.startPage(dpq.getPage() , dpq.getPageSize());
        List<DishVO> res = dishMapper.pageQuery(dpq);
        Integer size = dishMapper.pageQuerySize(dpq);
        return new PageResult(size , res);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatch(List<Long> ids) {
        // 判断能否删除 -- 菜品是否存在
        if(ids==null||ids.size()==0) throw new  DeletionNotAllowedException("菜品id 错误");
        // 判断能否删除 -- 菜品是否起售
        for(Long id:ids){
            System.out.println(id);
            int status = dishMapper.getStatusById(id);
            if( status == 1 )
                throw  new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
        // 判断能否删除 -- 菜品是否被套餐包含
        List<Long> setmeals = setMealDishMapper.getByDishIds(ids);
        if(setmeals!=null && setmeals.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 删除菜品
        for(Long id:ids){
            dishMapper.deleteById(id);
            // 是否有风味表 -- 连带删除
            dishFlavorMapper.deleteByDishId(id);
        }
    }

    @Override
    public void updateStatus(Long id ,Integer status){
        dishMapper.updateStatusById(id , status);
    }

    @Override
    public DishVO getDishById(Long id){
        DishVO dvo = dishMapper.getDishById(id);
        dvo.setFlavors(dishMapper.getFlavorsById(dvo.getId()));
        log.info("获取菜品的风味信息 {}" , dvo.getFlavors());
        return dvo;
    }

    @Transactional
    @Override
    public void updateDish(DishDTO dishDTO){
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        log.info("修改 dish {}" , dish);
        dishMapper.updateDish(dish);

        List<DishFlavor> flavors =  dishDTO.getFlavors();
        log.info("修改 口味 {}" , flavors);
        if(flavors!=null && flavors.size()>0){
            flavors.forEach(flavorDTO -> {flavorDTO.setDishId(dish.getId());});
            dishMapper.insertBanch( dishDTO.getFlavors() ) ;
        }
    }


}
