package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(OperationType.INSERT)
    void insertDish(Dish dish);

    void insertFlavor(DishFlavor dishFlavor);

    void insertBanch (List<DishFlavor>  dishFlavors);

    List<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    Integer pageQuerySize(DishPageQueryDTO dishPageQueryDTO);

    @Select("select  * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getFlavorsById(Long id);

    @Select("select status from dish where id = #{id}")
    Integer getStatusById(Long id);

    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    @Update("update dish set status = #{status}  where id = #{id} ;")
    void updateStatusById(Long id, Integer status);

    DishVO getDishById(Long id);

    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dis);
}
