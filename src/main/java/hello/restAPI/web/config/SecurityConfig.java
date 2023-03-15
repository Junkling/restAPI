package hello.restAPI.web.config;

import hello.restAPI.web.jwt.JwtAccessDeniedHandler;
import hello.restAPI.web.jwt.JwtAuthenticationEntryPoint;
import hello.restAPI.web.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtUtils jwtUtils;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf().disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 우리가 만든 클래스로 인증 실패 핸들링
                .accessDeniedHandler(jwtAccessDeniedHandler) // 커스텀 인가 실패 핸들링

                // enable h2-console // embedded h2를 위한 설정
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

//                .and()
//                .formLogin()
//                .loginPage("/api/users/login")
//                .usernameParameter("accountId")
//                .loginProcessingUrl("/loginProc")

                // api 경로
                .and()
                .authorizeRequests()
//                .antMatchers("/api/posts").hasAnyRole("Realtor","Lessor","Lessee")
//                .antMatchers("/api/hello").permitAll() // /api/hello
                .antMatchers("/api/users/login").permitAll() // 로그인 경로
                .antMatchers("/api/users/join").permitAll() // 회원가입 경로는 인증없이 호출 가능
                .anyRequest().authenticated() // 나머지 경로는 jwt 인증 해야함

                .and()
                .apply(new JwtSecurityConfig(jwtUtils)); // JwtSecurityConfig 적용

    }

}
