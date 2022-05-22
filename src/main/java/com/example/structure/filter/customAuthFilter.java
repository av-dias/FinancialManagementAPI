package com.example.structure.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.structure.userdetails.MyUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class customAuthFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public customAuthFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println(request.getParameter("username") + " " + request.getParameter("password"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        MyUserDetails user = (MyUserDetails) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("super-secret".getBytes());
        String access_token = JWT.create().
                withSubject(user.getUsername()).
                withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 10000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refresh_token = JWT.create().
                withSubject(user.getUsername()).
                withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 10000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        response.setHeader("access_token", access_token);
        response.setHeader("refresh_token", refresh_token);

    }


}
