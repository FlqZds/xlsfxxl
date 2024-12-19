package com.yunting.common.utils;//package com.yunting.gamemanager.common.utils;

import com.yunting.common.Dto.PlayerDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Data
public class JWTutil {

    //    @Value("${config.jwt.expiration}")
    private long expiration;
    //    @Value("${config.jwt.secret}")
    private String secret;

    private String token_header = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.";

    @Autowired
    public JWTutil() {
//        log.info("JWT初始化");
        expiration = 10800;
        secret = "nsdgdfhsagbjsdbfhjlatgfiuigufiugopdtuj";
    }

    /**
     * 加密 成 token
     */
    public String generateJwt_master(PlayerDTO player) {

        // 把秘钥变成byte 数组
        byte[] keyBytes = secret.getBytes();
        // 获得密钥对象
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        String token_row = Jwts.builder()
                .setHeaderParam("typ", "JWT") //令牌类型
                .setHeaderParam("alg", "HS256") //签名算法
                .setIssuedAt(new Date()) //签发时间
//                 设置过期时间为三个小时
                .setExpiration(new Date(System.currentTimeMillis() + (expiration * 1000)))  //令牌颁发后的三个小时
                .claim("playerId", player.getPlayerId())
                .claim("gameId", player.getGameId())
                .signWith(key, SignatureAlgorithm.HS256).compact();


        String[] split = token_row.split(token_header);

        return split[1];
    }


    /**
     * 把 token 解密成对象的算法
     */
    public PlayerDTO checkJwt_master(String jwtToken) {

        String token = token_header + jwtToken;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(this.secret.getBytes()).
                parseClaimsJws(token);
        // map
        Claims claims = claimsJws.getBody();
        Long playerId = claims.get("playerId", Long.class);
        Long gameId = claims.get("gameId", Long.class);


        return PlayerDTO.builder()
                .playerId(playerId)
                .gameId(gameId)
                .build();
    }


}
