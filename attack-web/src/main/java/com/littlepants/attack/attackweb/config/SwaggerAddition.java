package com.littlepants.attack.attackweb.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import com.littlepants.attack.attackweb.entity.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/9/22
 * @description
 */
@Component
public class SwaggerAddition implements ApiListingScannerPlugin {

    @Override
    public List<ApiDescription> apply(DocumentationContext documentationContext) {
        //登录接口
        //1.定义参数
        Parameter user = new ParameterBuilder()
                .name("user")
                .description("参考密码AttackWeb123")
                .type(new TypeResolver().resolve(User.class))
                .modelRef(new ModelRef("User"))
                .parameterType("body")
                .required(true)
                .build();
        //2.接口的每种请求方式(GET/POST...)为一个 Operation
        Operation loginOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("登录")
                .tags(Sets.newHashSet("Authentication"))
                .responseMessages(Sets.newHashSet(new ResponseMessageBuilder().code(200).message("OK").build()))
                .consumes(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .parameters(Collections.singletonList(user))
                .build();
        //3.每个接口路径对应一个 ApiDescription
        ApiDescription loginDesc = new ApiDescription(null, "/login", "登录", Collections.singletonList(loginOperation), false);

        //登出接口
        Operation logoutOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.GET)
                .summary("登出")
                .notes("退出登录")
                .tags(Sets.newHashSet("Authentication"))
                .responseMessages(Sets.newHashSet(new ResponseMessageBuilder().code(200).message("OK").build()))
                .build();
        ApiDescription logoutDesc = new ApiDescription(null, "/logout", "注销", Collections.singletonList(logoutOperation), false);

        documentationContext.getTags().add(new Tag("Authentication", "登录、登出"));

        List<ApiDescription> apiDescriptionList = new ArrayList<>(Arrays.asList(loginDesc, logoutDesc));
        return apiDescriptionList;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }

}
