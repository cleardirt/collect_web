package com.example.demo.util.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.enums.UserRole;
import com.example.demo.po.user.User;
import com.example.demo.util.MyException;
import com.example.demo.vo.user.UserFormVO;
import org.apache.commons.lang.StringUtils;

import java.util.Calendar;

/**
 * @author admin
 */
public class JWTUtil {

    /**
     * 获取token
     * @param u user
     * @return token
     */
    public static String getToken(UserFormVO u) {
        Calendar instance = Calendar.getInstance();
        //默认令牌过期时间7天
        instance.add(Calendar.DATE, 7);
        String jwtSecret="JwtSecret";
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("userId", u.getId())
                .withClaim("userPhone",u.getPhone())
                .withClaim("username", u.getUsername())
                .withClaim("userRole",u.getUserRole());

        return builder.withExpiresAt(instance.getTime())
                .sign(algorithm);
    }

    /**
     * 验证token合法性 成功返回token
     */
    public static DecodedJWT verify(String token) throws MyException {
        if(StringUtils.isEmpty(token)){
            throw new MyException("token不能为空");
        }
        Algorithm algorithm = Algorithm.HMAC256("JwtSecret");
        JWTVerifier build = JWT.require(algorithm).build();
        return build.verify(token);
    }

}
