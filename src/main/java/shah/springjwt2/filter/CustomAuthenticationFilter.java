package shah.springjwt2.filter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;
import shah.springjwt2.jwt.AccessTokenGenerator;
import shah.springjwt2.jwt.RefreshTokenGenerator;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

        private final AuthenticationManager authenticationManager;

        public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
                this.authenticationManager = authenticationManager;
        }

        // A) AUTHENTICATE LOGIN DETAILS USING OUR CUSTOM USERDETAILS SERVICE
        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                        throws AuthenticationException {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                log.info("Username is: {}", username);
                log.info("Password is: {}", password);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                username, password);
                return authenticationManager.authenticate(authenticationToken);
        }

        // IF ABOVE VALIDATION SUCCESS, CREATE JWT HERE
        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                        FilterChain chain, Authentication authentication) throws IOException, ServletException {

                User user = (User) authentication.getPrincipal();

                // CREATE ACCESS TOKEN
                String access_token = AccessTokenGenerator.generate(user, request);
                // CREATE REFRESH TOKEN
                String refresh_token = RefreshTokenGenerator.generate(user, request);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        }
}
