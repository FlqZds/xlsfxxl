package com.yunting.common.interceptor;

import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.utils.JWTutil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;

@SpringBootConfiguration
@Component("ClientInterceptor")
@Slf4j
public class ClientInterceptor implements HandlerInterceptor {


    @Resource(name = "JWTutil")
    private JWTutil jwTutil;

    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        log.info("进入client拦截器");

        String authorizationTOKEN = request.getHeader("Authorization");
        PlayerDTO playerDTO = validateToken(authorizationTOKEN); //校验token
        if (playerDTO==null){
            log.error("登录无效，用户被封禁");
            throw new AppException(ResponseEnum.BAN_USER_OUT);
        }


        request.setAttribute("playerDTO", playerDTO);

        return true;
    }



    /***
     * 校验token是否有效，(异地登录)，并解析
     * @param jwt
     * @return masterDTO 解析后的管理员信息
     */
    @Transactional(rollbackFor = Exception.class)
    public PlayerDTO validateToken(String jwt) {
        //没有携带token就是没有登录
        if (StringUtils.isBlank(jwt)) {
            log.error("token为空导致的请求失败");
            throw new AppException(ResponseEnum.USER_NO_LOGIN);
        }

        PlayerDTO playerDTO = jwTutil.checkJwt_master(jwt);
        Long playerId = playerDTO.getPlayerId();

        playerDTO.setPlayerToken(jwt);

        return playerDTO;
    }


}
