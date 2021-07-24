package com.example.blog_kim_s_token.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.blog_kim_s_token.model.coolSms.coolSmsDto;
import com.example.blog_kim_s_token.model.user.singupDto;
import com.example.blog_kim_s_token.service.coolSmsService;
import com.example.blog_kim_s_token.service.userService;
import com.example.blog_kim_s_token.service.utillService;
import com.example.blog_kim_s_token.service.ApiServies.kakao.kakaoLoginservice;
import com.example.blog_kim_s_token.service.ApiServies.naver.naverLoginService;
import com.nimbusds.jose.shaded.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class restcontroller {

    @Autowired
    private userService userService;
    @Autowired
    private coolSmsService coolSmsService;
    @Autowired
    private utillService utillService;
    @Autowired
    private naverLoginService naverLoingService;
    @Autowired
    private kakaoLoginservice kakaoLoginservice;

    @RequestMapping("/auth/confrimEmail")
    public boolean confrimEmail(HttpServletRequest request,HttpServletResponse response) {
        return userService.confrimEmail((String)request.getParameter("email"));
    }
    @RequestMapping("/auth/confrimPhoneNum")
    public boolean confrimPhoneNum(HttpServletRequest request,HttpServletResponse response) {
        return userService.confrimPhone((String)request.getParameter("phoneNum"));
    }
    @RequestMapping("/auth/sendSms")
    public boolean sendSms(@RequestBody coolSmsDto coolSmsDto,HttpServletResponse response,HttpSession httpSession) {
        String SmsNum=utillService.GetRandomNum(6);
        httpSession.setAttribute("insertPhone", coolSmsDto.getPhoneNum());
        httpSession.setAttribute("insertRandNum", SmsNum);
        return coolSmsService.sendMessege(coolSmsDto.getPhoneNum(),"인증번호는 "+SmsNum+"입니다");
    }
    @RequestMapping("/auth/insertUser")
    public JSONObject insertUser(@RequestBody singupDto singupDto,HttpSession httpSession) {
        return userService.insertUser(singupDto,httpSession);
    }
    @RequestMapping("/auth/index2")
    public String hello2(@CookieValue(value = "refreshToken", required = false) Cookie rCookie,HttpServletResponse response) {
        System.out.println("index2");
        System.out.println(rCookie.getValue());
        return "index2";
    }
    @RequestMapping("/auth/naver")
    public String naverLogin() {
        return  naverLoingService.naverLogin();
    }
    @RequestMapping("/auth/kakao")
    public String kakaoLogin(HttpServletRequest request,HttpServletResponse response) {
        return kakaoLoginservice.kakaoGetCode();
    }
    @RequestMapping("/api/v1/user/test")
    public JSONObject  user(HttpServletRequest request,HttpServletResponse response) {
        System.out.println("user 입장");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("role", "user");
        jsonObject.put("hello", "world");
        return jsonObject;
    }
    @RequestMapping("/api/v1/manage/test")
    public String  manage(HttpServletRequest request,HttpServletResponse response) {
        System.out.println("manage 입장");
        return "manage";
    }
    @RequestMapping("/api/v1/admin/test")
    public String  admin(HttpServletRequest request,HttpServletResponse response) {
        System.out.println("admin 입장");
        return "admin";
    }
    
}
