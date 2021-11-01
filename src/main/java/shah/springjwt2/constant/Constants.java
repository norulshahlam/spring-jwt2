package shah.springjwt2.constant;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.auth0.jwt.algorithms.Algorithm;

public class Constants {

    public static final String JWT_SECRET="secret";
    public static final Date ACCESS_TOKEN_VALIDITY=new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1));
    public static final Date REFRESH_TOKEN_VALIDITY=new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
    public static final Algorithm ALGORITHM= Algorithm.HMAC256(JWT_SECRET.getBytes());
}
