package com.littlepants.attack.attackplus.controller;

import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  Admin
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/26
 */

@RestController
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
