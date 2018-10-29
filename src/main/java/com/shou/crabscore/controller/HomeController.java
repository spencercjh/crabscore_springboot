package com.shou.crabscore.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 测试首页
 *
 * @author spencercjh
 */
@Log4j2
@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "/home.html";
    }
}
