package com.fw.core.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fw.core.config.adapter.RedisAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;


public class CustomTokenFilter extends OncePerRequestFilter {
	private static final String TOKEN_PREFIX = "Bearer ";
	private RedisAdapter redisAdapter;

	public CustomTokenFilter(RedisAdapter redisAdapter) {
		this.redisAdapter = redisAdapter;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = request.getHeader("Authorization");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (token != null && token.startsWith(TOKEN_PREFIX) && authentication.getPrincipal() instanceof Jwt) {
			String accessToken = token.substring(TOKEN_PREFIX.length());
			String key = "expireToken::" + accessToken.hashCode();
			Jwt jwt =(Jwt) authentication.getPrincipal();
			if (!this.redisAdapter.exists(key) && !jwt.getClaims().containsKey("client_id") && !jwt.getClaims().containsKey("clientId")) {
				new SecurityContextLogoutHandler().logout(request, response, authentication);
				SecurityContextHolder.getContext().setAuthentication(null);
				authentication.setAuthenticated(false);
			}
		}
		filterChain.doFilter(request, response);
	}

}
