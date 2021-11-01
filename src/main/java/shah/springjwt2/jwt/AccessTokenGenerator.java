package shah.springjwt2.jwt;

import static shah.springjwt2.constant.Constants.ACCESS_TOKEN_VALIDITY;
import static shah.springjwt2.constant.Constants.ALGORITHM;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.JWT;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AccessTokenGenerator {

    public AccessTokenGenerator() {
    }

    public static String generate(User user, HttpServletRequest request) {
        String access_token = JWT
        .create()
        .withSubject(user.getUsername())
        .withExpiresAt(ACCESS_TOKEN_VALIDITY)
        .withIssuer(request.getRequestURL().toString())
        .withClaim("roles",
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList()))
        .withClaim("Owner", "Norulshahlam").sign(ALGORITHM);
        return access_token;
    }
}