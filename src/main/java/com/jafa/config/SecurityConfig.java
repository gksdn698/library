package com.jafa.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.jafa.security.CustomUserDetailService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	AccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
	AuthenticationFailureHandler authenticationFailureHandler;
	
	@Autowired
	CustomUserDetailService customUserDetailService;
	
	@Autowired
	DataSource dataSource;
	
	@Override
		protected void configure(HttpSecurity http) throws Exception {
		
			CharacterEncodingFilter filter = new CharacterEncodingFilter(); 
			filter.setEncoding("utf-8");
			filter.setForceEncoding(true);
			http.addFilterBefore(filter, CsrfFilter.class);
		
			// 로그인하지 않은 경우 로그인페이지로 이동
			http.formLogin()
				.loginPage("/login")
				.usernameParameter("memberId")
				.passwordParameter("memberPwd")		
				.successHandler(authenticationSuccessHandler)
				.failureHandler(authenticationFailureHandler);
			
			// 403에러 처리 페이지
			http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
			
			// 자동로그인
			http.rememberMe().key("park")
				.tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(604800);
			
			// 로그아웃
			http.logout().logoutUrl("/member/logout").invalidateHttpSession(true)
				.deleteCookies("remember-me","JSESSION_ID");
		}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailService)
			.passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		jdbcTokenRepositoryImpl.setDataSource(dataSource);
		return jdbcTokenRepositoryImpl;
	}
	
}
