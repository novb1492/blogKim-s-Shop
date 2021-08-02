package com.example.blog_kim_s_token.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.blog_kim_s_token.config.principaldetail;
import com.example.blog_kim_s_token.model.jwt.jwtDto;
import com.example.blog_kim_s_token.model.user.userDto;
import com.example.blog_kim_s_token.service.cookie.cookieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class jwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    
    private jwtService jwtService;
    
    private cookieService cookieService;

    public jwtLoginFilter(jwtService jwtService,cookieService cookieService ){
        this.jwtService=jwtService;
        this.cookieService=cookieService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)throws AuthenticationException {
        System.out.println("로그인요청 attemptAuthentication  ");
        try {
  
            ObjectMapper objectMapper=new ObjectMapper();
            userDto userDto=objectMapper.readValue(request.getInputStream(), userDto.class);
            System.out.println(userDto);
            
            Authentication authentication=jwtService.confrimAuthenticate(userDto);
            jwtService.setSecuritySession(authentication);
            
            System.out.println("로그인완료"+authentication.getName());

            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 입장");

        principaldetail principaldetail=(principaldetail)authResult.getPrincipal();
        String jwtToken=jwtService.getJwtToken(principaldetail.getUserDto().getId());
        jwtDto jwtDto=jwtService.getRefreshToken(principaldetail.getUserDto().getId());
        String refreshToken=jwtService.getRefreshToken(jwtDto,principaldetail.getUserDto().getId());
        
        System.out.println(jwtToken);
        String[] cookiesNames={"Authorization","refreshToken"};
        String[] cookiesValues={jwtToken,refreshToken};
        cookieService.cookieFactory(response, cookiesNames, cookiesValues);

        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,AuthenticationException failed) throws IOException, ServletException {
        System.out.println("로그인 실패");
        System.out.println(failed.getCause()+failed.getLocalizedMessage()+failed.getStackTrace()+failed.getSuppressed());
        RequestDispatcher dp=request.getRequestDispatcher("/login");
		dp.forward(request, response);

    }
}
