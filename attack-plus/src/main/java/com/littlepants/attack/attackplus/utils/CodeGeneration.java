package com.littlepants.attack.attackplus.utils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2023/4/20
 * @description
 */
public class CodeGeneration {
    private final static String dirPath = "D:\\Projects\\attack-plus\\src\\main\\java";
    private final static String author = "厕所歌神李狗蛋";
    private final static String packageName = "com.attack.attackplus";
    private final static String serviceTemplatePath = "templates/att_service.java.vm";
    private final static String serviceImplTemplatePath = "templates/att_serviceImpl.java.vm";
    private final static String entityTemplatePath = "templates/att_entity.java.vm";
    private final static String controllerTemplatePath = "templates/att_controller.java.vm";
    private final static String mapperXmlTemplatePath = "templates/att_mapper.xml.vm";
    private final static String mapperTemplatePath = "templates/att_mapper.java.vm";
    private final static String controllerName = "%sController";
    private final static String serviceName = "%sService";
    private final static String serviceImplName = "%sServiceImpl";
    private final static String mapperName = "%sDao";
    private final static String mapperXmlName = "%sMapper";

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        InputStream in = new FileInputStream("D:\\Projects\\attack-plus\\src\\main\\resources\\application-dev.properties");
        properties.load(in);
        String url = properties.getProperty("spring.datasource.url");
        String username = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");

        DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig.Builder(url, username, password).dbQuery(new MySqlQuery())
                .typeConvert(new MySqlTypeConvert(){
                    @Override
                    public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
                        if (fieldType.contains("tinyint"))
                            return DbColumnType.BOOLEAN;
                        else if (fieldType.contains("bigint"))
                            return DbColumnType.LONG;
                        else if (fieldType.contains("int"))
                            return DbColumnType.INTEGER;
                        else if (fieldType.contains("float")||fieldType.contains("double"))
                            return DbColumnType.DOUBLE;
                        else if (fieldType.contains("datetime"))
                            return DbColumnType.LOCAL_DATE_TIME;
                        return super.processTypeConvert(config, fieldType);
                    }
                });
//        String[] tables = {"att_testcase_caldera","att_testcase_atomic","att_testcase_ms"};
        String[] tables = {"att_case_ip","att_case_operation","att_case_tools","att_testcase_campaign"};
        createService(DATA_SOURCE_CONFIG,tables);
    }

    /**
     * 生成不需要Controller的表代码
     * @param DATA_SOURCE_CONFIG
     * @param tables
     */
    public static void createService(DataSourceConfig.Builder DATA_SOURCE_CONFIG,String[] tables){
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> {builder.outputDir(dirPath).author(author).disableOpenDir()
                        .enableSwagger().fileOverride();})
                .packageConfig(builder -> {builder.parent(packageName);})
                .strategyConfig(builder -> {builder.addInclude(tables).addTablePrefix("att_")
                        .entityBuilder().enableLombok().enableTableFieldAnnotation().idType(IdType.ASSIGN_ID)
                        .serviceBuilder().formatServiceFileName("%sService").formatServiceImplFileName("%sServiceImpl")
                        .mapperBuilder().formatMapperFileName("%sDao").formatXmlFileName("%sMapper").enableMapperAnnotation();})
                .templateConfig(builder -> {builder.disable()
                        .service(serviceTemplatePath)
                        .serviceImpl(serviceImplTemplatePath)
                        .entity(entityTemplatePath)
                        .mapperXml(mapperXmlTemplatePath)
                        .mapper(mapperTemplatePath)
                        .build();})
                .execute();
    }

    /**
     * 生成关系类
     * @param DATA_SOURCE_CONFIG
     * @param tables
     */
    public static void createRelation(DataSourceConfig.Builder DATA_SOURCE_CONFIG,String[] tables){
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> {builder.outputDir(dirPath).author(author).disableOpenDir()
                        .enableSwagger().fileOverride();})
                .packageConfig(builder -> {builder.parent(packageName);})
                .strategyConfig(builder -> {builder.addInclude(tables).addTablePrefix("att_")
                        .entityBuilder().enableLombok().enableTableFieldAnnotation().idType(IdType.ASSIGN_ID)
                        .mapperBuilder().formatMapperFileName(mapperName).formatXmlFileName(mapperXmlName).enableMapperAnnotation();})
                .templateConfig(builder -> {builder.disable()
                        .entity(entityTemplatePath)
                        .mapper(mapperTemplatePath)
                        .mapperXml(mapperXmlTemplatePath)
                        .build();})
                .execute();
    }

    /**
     * 生成完全类
     * @param DATA_SOURCE_CONFIG
     * @param tables
     */
    public static void createEntity(DataSourceConfig.Builder DATA_SOURCE_CONFIG,String[] tables){
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> {builder.outputDir(dirPath).author(author).disableOpenDir()
                        .enableSwagger().fileOverride();})
                .packageConfig(builder -> {builder.parent(packageName);})
                .strategyConfig(builder -> {builder.addInclude(tables).addTablePrefix("att_")
//                        .entityBuilder().enableLombok().enableTableFieldAnnotation().idType(IdType.ASSIGN_ID)
                        .controllerBuilder().formatFileName(controllerName).enableRestStyle()
                        .serviceBuilder().formatServiceFileName(serviceName).formatServiceImplFileName(serviceImplName)
                        .mapperBuilder().formatMapperFileName(mapperName).formatXmlFileName(mapperXmlName).enableMapperAnnotation();})
                .templateConfig(builder -> {builder.disable()
                        .service(serviceTemplatePath)
                        .serviceImpl(serviceImplTemplatePath)
//                        .entity(entityTemplatePath)
                        .controller(controllerTemplatePath)
                        .mapper(mapperTemplatePath)
                        .mapperXml(mapperXmlTemplatePath)
                        .build();})
                .execute();
    }

    public static void createTopology(DataSourceConfig.Builder DATA_SOURCE_CONFIG){
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> {builder.outputDir(dirPath).author(author).disableOpenDir()
                        .enableSwagger().fileOverride();})
                .packageConfig(builder -> {builder.parent(packageName);})
                .strategyConfig(builder -> {builder.addInclude("att_topology").addTablePrefix("att_")
                        .entityBuilder().enableLombok().enableTableFieldAnnotation().idType(IdType.ASSIGN_ID)
                        .controllerBuilder().formatFileName(controllerName).enableRestStyle()
                        .serviceBuilder().formatServiceFileName(serviceName).formatServiceImplFileName(serviceImplName)
                        .mapperBuilder().formatMapperFileName(mapperName).formatXmlFileName(mapperXmlName).enableMapperAnnotation();})
                .templateConfig(builder -> {builder.disable()
                        .service(serviceTemplatePath)
                        .serviceImpl(serviceImplTemplatePath)
                        .entity(entityTemplatePath)
                        .controller(controllerTemplatePath)
                        .mapper(mapperTemplatePath)
                        .mapperXml(mapperXmlTemplatePath)
                        .build();})
                .execute();
    }

    public static void createUserRole(DataSourceConfig.Builder DATA_SOURCE_CONFIG){
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> {builder.outputDir(dirPath).author(author).disableOpenDir()
                        .enableSwagger().fileOverride();})
                .packageConfig(builder -> {builder.parent(packageName);})
                .strategyConfig(builder -> {builder.addInclude("att_user_role").addTablePrefix("att_")
                        .entityBuilder().enableLombok().enableTableFieldAnnotation().idType(IdType.ASSIGN_ID)
                        .mapperBuilder().formatMapperFileName(mapperName).formatXmlFileName(mapperXmlName).enableMapperAnnotation();})
                .templateConfig(builder -> {builder.disable()
                        .entity(entityTemplatePath)
                        .mapper(mapperTemplatePath)
                        .mapperXml(mapperXmlTemplatePath)
                        .build();})
                .execute();
    }

    public static void createRole(DataSourceConfig.Builder DATA_SOURCE_CONFIG){
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> {builder.outputDir(dirPath).author(author).disableOpenDir()
                        .enableSwagger().fileOverride();})
                .packageConfig(builder -> {builder.parent(packageName);})
                .strategyConfig(builder -> {builder.addInclude("att_role").addTablePrefix("att_")
                        .entityBuilder().enableLombok().enableTableFieldAnnotation().idType(IdType.ASSIGN_ID)
                        .controllerBuilder().formatFileName(controllerName).enableRestStyle()
                        .serviceBuilder().formatServiceFileName(serviceName).formatServiceImplFileName(serviceImplName)
                        .mapperBuilder().formatMapperFileName(mapperName).formatXmlFileName(mapperXmlName).enableMapperAnnotation();})
                .templateConfig(builder -> {builder.disable()
                        .service(serviceTemplatePath)
                        .serviceImpl(serviceImplTemplatePath)
                        .entity(entityTemplatePath)
                        .controller(controllerTemplatePath)
                        .mapper(mapperTemplatePath)
                        .mapperXml(mapperXmlTemplatePath)
                        .build();})
                .execute();
    }

    public static void createLog(DataSourceConfig.Builder DATA_SOURCE_CONFIG){
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> {builder.outputDir(dirPath).author(author).disableOpenDir()
                        .enableSwagger().fileOverride();})
                .packageConfig(builder -> {builder.parent(packageName);})
                .strategyConfig(builder -> {builder.addInclude("att_opt_log").addInclude("att_login_log")
                        .addTablePrefix("att_")
                        .entityBuilder().enableLombok().enableTableFieldAnnotation().idType(IdType.ASSIGN_ID)
                        .serviceBuilder().formatServiceFileName("%sService").formatServiceImplFileName("%sServiceImpl")
                        .mapperBuilder().formatMapperFileName("%sDao").formatXmlFileName("%sMapper").enableMapperAnnotation();})
                .templateConfig(builder -> {builder.disable()
                        .service(serviceTemplatePath)
                        .serviceImpl(serviceImplTemplatePath)
                        .entity(entityTemplatePath)
                        .mapperXml(mapperXmlTemplatePath)
                        .mapper(mapperTemplatePath)
                        .build();})
                .execute();
    }

    public static void createUser(DataSourceConfig.Builder DATA_SOURCE_CONFIG){
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> {builder.outputDir(dirPath).author("厕所歌神李狗蛋").disableOpenDir()
                        .enableSwagger().fileOverride();})
                .packageConfig(builder -> {builder.parent("com.attack.attackplus");})
                .strategyConfig(builder -> {builder.addInclude("att_user").addTablePrefix("att_")
                        .entityBuilder().enableLombok().enableTableFieldAnnotation().idType(IdType.ASSIGN_ID)
                        .addTableFills(new Column("create_time", FieldFill.INSERT))
                        .addTableFills(new Property("updateTime",FieldFill.INSERT_UPDATE))
                        .controllerBuilder().formatFileName("%sController").enableRestStyle()
                        .serviceBuilder().formatServiceFileName("%sService").formatServiceImplFileName("%sServiceImpl")
                        .mapperBuilder().formatMapperFileName("%sDao").formatXmlFileName("%sMapper").enableMapperAnnotation();})
                .templateConfig(builder -> {builder.disable()
                        .service("templates/att_service.java.vm")
                        .serviceImpl("templates/att_serviceImpl.java.vm")
                        .entity("templates/att_entity.java.vm")
                        .controller("templates/att_controller.java.vm")
                        .mapperXml("templates/att_mapper.xml.vm")
                        .mapper("templates/att_mapper.java.vm")
                        .build();})
                .execute();
    }
}
