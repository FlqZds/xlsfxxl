package com.yunting.client.common.config;


import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static com.yunting.client.common.utils.FS.*;


@Configuration
public class Swagger2Config {

    /**
     * 定义接口的总体信息
     *
     * @return
     */
//客户端
    private ApiInfo clientApiInfo() {

        return new ApiInfoBuilder()
                .title(CLient_Title)
                .description(CLient_Description)
                .version("8.6.7")
                .termsOfServiceUrl("http://www.4399.com")
                .contact(new Contact("张德胜", "http://www.baidu.com", "13348683927@163.com"))
                .build();
    }

    @Bean
    public Docket clientConnectApiConfig() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("游戏设置") // 管路⚪组
                .apiInfo(clientApiInfo()) // 组的信息
                .select()       // 通过.select()方法，去配置扫描接口，支持使用多个.paths()
                //只显示Client路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/Client/.*")))
                .build();

    }

//-------------------------------------------------分割符-----------------------------------------------------------------------

//用户相关

    private ApiInfo playerApiInfo() {

        return new ApiInfoBuilder()
                .title(Admin_Title)
                .description(Admin_Description)
                .version("1.0")
                .contact(new Contact("张德胜", "http://www.baidu.com", "13348683927@163.com"))
                .build();
    }

    @Bean
    public Docket playerApiConfig() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户相关") // 管路⚪组
                .apiInfo(playerApiInfo()) // 组的信息
                .select()       // 通过.select()方法，去配置扫描接口，支持使用多个.paths()
                //只显示Client路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/player/.*")))
                .build();

    }

    //-------------------------------------------------分割符----------------------------------------------------------------------

    /**
     * 定义接口的总体信息
     *
     * @return
     */
//客户端
    private ApiInfo AdvRecordApiInfo() {

        return new ApiInfoBuilder()
                .title(AdvRecord_Title)
                .description(AdvRecord_Description)
                .version("1.0")
                .contact(new Contact("张德胜", "http://www.baidu.com", "13348683927@163.com"))
                .build();
    }

    @Bean
    public Docket AdvRecordConnectApiConfig() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("广告记录组") // 管路⚪组
                .apiInfo(AdvRecordApiInfo()) // 组的信息
                .select()       // 通过.select()方法，去配置扫描接口，支持使用多个.paths()
                //只显示Client路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/Advertise/.*")))
                .build();

    }

//-------------------------------------------------分割符----------------------------------------------------------------------

    /**
     * 定义接口的总体信息
     *
     * @return
     */
//客户端
    private ApiInfo WithDrawApiInfo() {

        return new ApiInfoBuilder()
                .title("客户端提现申请")
                .description("提现接口对接")
                .version("1.0")
                .contact(new Contact("zds", "http://www.baidu.com", "13348683927@163.com"))
                .build();
    }

    @Bean
    public Docket WithDrawConnectApiConfig() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("提现接口") // 管路⚪组
                .apiInfo(AdvRecordApiInfo()) // 组的信息
                .select()       // 通过.select()方法，去配置扫描接口，支持使用多个.paths()
                //只显示Client路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/Withdraw/cl/.*")))
                .build();

    }

}