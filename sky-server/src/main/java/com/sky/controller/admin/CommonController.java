package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@Api( tags = "通用接口")
@Slf4j
@RestController
@RequestMapping("admin/common")
public class CommonController {

    @Autowired
    AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("文件上传 {}"  ,file);
        try{
            String OrginalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + OrginalFileName.substring(OrginalFileName.lastIndexOf("."));
            String imgUrl = aliOssUtil.upload(file.getBytes() , fileName );
            return Result.success(imgUrl);
        }catch (Exception e){
            log.error("文件上传失败");
        }
        return Result.error("文件上传失败");
    }
}
