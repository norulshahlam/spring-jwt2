package shah.springjwt2.jwt;

import static shah.springjwt2.constant.Constants.ALGORITHM;
import static shah.springjwt2.constant.Constants.REFRESH_TOKEN_VALIDITY;

import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.JWT;

import org.springframework.security.core.userdetails.User;
public class RefreshTokenGenerator {

    public RefreshTokenGenerator() {
    }

    public static String generate(User user,HttpServletRequest request ){
        String refresh_token = JWT.create().withSubject(user.getUsername())
        .withExpiresAt(REFRESH_TOKEN_VALIDITY).withIssuer(request.getRequestURL().toString())
        .sign(ALGORITHM);
        return refresh_token;
    }
    

}
