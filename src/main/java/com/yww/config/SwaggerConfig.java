package com.yww.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;

/**
 * <p>
 *     Swagger的配置类
 * </p>
 *
 * @ClassName SwaggerConfig
 * @Author yww
 * @Date 2021/3/2 9:35
 * @Version 1.0
 **/

@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    private final OpenApiExtensionResolver openApiExtensionResolver;

    @Autowired
    public SwaggerConfig(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    @Bean
    public Docket docket(){
        //调用apiInfo方法来传入信息
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yww.control"))
                .build()
                .extensions(openApiExtensionResolver.buildExtensions("yww"));
    }

    /**
     * @Descriprtion 自定义信息
     * @return ApiInfo
     */
    private ApiInfo apiInfo(){
        //创建一个contact对象以便输入
        Contact contact = new Contact("yw", "localhost:8080/", "1141950370@qq.com");
        return new ApiInfo(
                "Yw的swagger文档",
                "永远相信美好的事情即将发生",
                "v1.0",
                "urn:tos",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LINCENSE-2.0 ",
                new ArrayList<>());
    }

}
