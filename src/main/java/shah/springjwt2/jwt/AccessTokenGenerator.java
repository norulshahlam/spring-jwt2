package shah.springjwt2.jwt;

import com.auth0.jwt.JWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;

import static shah.springjwt2.constant.Constants.ACCESS_TOKEN_VALIDITY;
import static shah.springjwt2.constant.Constants.ALGORITHM;

public class AccessTokenGenerator {

    public AccessTokenGenerator() {
    }

    public static String generate(User user, HttpServletRequest request) {
        return JWT
        .create()
        .withSubject(user.getUsername())
        .withExpiresAt(ACCESS_TOKEN_VALIDITY)
        .withIssuer(request.getRequestURL().toString())
        .withClaim("roles",
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .toList())
        .withClaim("Owner", "Norulshahlam").sign(ALGORITHM);
    }
}