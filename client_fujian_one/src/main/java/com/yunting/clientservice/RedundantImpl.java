package com.yunting.clientservice;

import com.yunting.client.DTO.PlayerDTO;
import com.yunting.client.common.exception.AppException;
import com.yunting.client.common.results.ResponseEnum;
import com.yunting.client.entity.Application;
import com.yunting.client.mapper.ApplicationMapper;
import com.yunting.clientservice.service.RedundantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service("RedundantService")
public class RedundantImpl implements RedundantService {

    @Resource(name = "ApplicationMapper")
    ApplicationMapper applicationMapper;

    //拿到所有应用
    @Override
    public List getMoreEntertainment() {
        List<Application> applications = applicationMapper.selectAll();
        if (applications.isEmpty()) {
            log.warn("未找到任何其他应用", new AppException(ResponseEnum.NO_ANY_REDUNDANT_APP));
            throw new AppException(ResponseEnum.NO_ANY_REDUNDANT_APP);
        }
        return applications;
    }

    @Override
    public void download(PlayerDTO playerDTO) {

    }


}
