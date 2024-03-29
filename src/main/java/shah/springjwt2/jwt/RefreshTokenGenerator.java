package shah.springjwt2.jwt;

import com.auth0.jwt.JWT;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;

import static shah.springjwt2.constant.Constants.ALGORITHM;
import static shah.springjwt2.constant.Constants.REFRESH_TOKEN_VALIDITY;
public class RefreshTokenGenerator {

    public RefreshTokenGenerator() {
    }

    public static String generate(User user,HttpServletRequest request ){
        return JWT.create().withSubject(user.getUsername())
        .withExpiresAt(REFRESH_TOKEN_VALIDITY).withIssuer(request.getRequestURL().toString())
        .sign(ALGORITHM);
    }
}
