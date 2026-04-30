package com.mango.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.ibatis.type.JdbcType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CodeGenerator {


    /**
     * 以下是定义的map的key ，不需要修改。每次运行前，只需修改main方法的中的map参数
     */
    public static final String DATABASE = "database";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String URL = "url";
    public static final String TARGET_MODULE = "targetModule";
    public static final String JAVA_ROOT_PACKAGE = "javaRootPackage";

    public static void main(String[] args) {
        // 数据库连接信息，勿改
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(URL, "jdbc:mysql://127.0.0.1:3307/");   // 数据库地址
        paramMap.put(USERNAME, "root");     // 数据库用户名
        paramMap.put(PASSWORD, "123456");   // 数据库密码

        // 1，生成所有java代码(controller,service,mapper等)，生成哪个库的表的代码，只需修改参数和模块即可
        // 去掉覆盖，防止误点
        paramMap.put(DATABASE, "mg_bkl"); // 数据库名称
        paramMap.put(TARGET_MODULE, "mango-bkl");  // 生成代码的模块
        paramMap.put(JAVA_ROOT_PACKAGE, "com.mango.bkl");  // 生成代码的模块的根包名

        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.generateAllCodeOverride(paramMap);
    }

    private void generateAllCodeOverride(Map<String, String> paramMap) {

        // 1，数据库配置
        String url = paramMap.get(URL) + paramMap.get(DATABASE) +
                "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
        String username = paramMap.get(USERNAME);
        String password = paramMap.get(PASSWORD);

        // 2. 项目路径配置
        // Java 标准 API，用于项目的根目录，会自动带出当前项目目录。例如：/Users/xiongdada/IdeaProjects/mango-mes。注意：是user.dir，不是usr.dir
        String projectRoot = System.getProperty("user.dir");

        // 代码生成的目标模块，例如 mango-bkl
        String targetModule = paramMap.get(TARGET_MODULE);
        // 生成代码的目录
        String outputJavaDir = projectRoot + "/" + targetModule + "/src/main/java";
        // 生成mapper的目录
        String outputMapperDir = projectRoot + "/" + targetModule + "/src/main/resources/mapper";

        // 数据库表的命名规则，不是这个命名规则就无法生成。这个是自己定制的规则，表名以t_开头
        String includeTable = "^t_.*";
//        String includeTable = "t_material";

        FastAutoGenerator.create(url, username, password)
                // 3，全局配置
                .globalConfig(builder -> {
                    builder.author("Hu_jie")    // 设置作者
                            .outputDir(outputJavaDir)   // 输出目录
                            .dateType(DateType.TIME_PACK)   // 时间策略
                            .commentDate("yyyy-MM-dd") // 注释日期格式
                            .disableOpenDir();         //生成后不打开输出目录
                })
                // 4，包配置
                .packageConfig(builder -> {
                    builder.parent(paramMap.get(JAVA_ROOT_PACKAGE))  // 目标微服务的根包名
                            .entity("entity") // 设置 Entity 包名
                            .service("service") // 设置 Service 包名
                            .serviceImpl("service.impl") // 设置 Service Impl 包名
                            .mapper("mapper") // 设置 Mapper 包名
//                            .xml("mappers") // 设置 Mapper XML 包名
                            .controller("controller") // 设置 Controller 包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, outputMapperDir));
                })
                // 5，配置策略
                .strategyConfig(builder -> {
                    builder.enableCapitalMode()  // 开启大写命名
                            .addInclude(includeTable) // 设置需要生成的表名
                            .addTablePrefix("t_") // 设置过滤表前缀
                            .enableSkipView() // 开启跳过视图

                            // Entity策略
                            .entityBuilder()
                            .enableLombok()                                  // 启用lombok
                            .naming(NamingStrategy.underline_to_camel)       // 表名->实体名策略
                            .columnNaming(NamingStrategy.underline_to_camel) // 字段名策略
                            // 设置雪花算法ID
                            .idType(IdType.ASSIGN_ID)
//                            .enableFileOverride()         // 覆盖已经生成的代码
                            .enableTableFieldAnnotation() // 生成字段注解

                            // Service策略
                            .serviceBuilder()
//                            .enableFileOverride()                       // 覆盖已经生成的代码
                            .formatServiceFileName("%sService")         // 格式化生成的service接口名
                            .formatServiceImplFileName("%sServiceImpl") // 格式化生成的service实现类名

                            // Controller策略
                            .controllerBuilder()
                            .enableRestStyle()           // RESTful风格
//                            .enableFileOverride()        // 覆盖已经生成的代码

                            // Mapper策略
                            .mapperBuilder()
                            .enableMapperAnnotation()        // 启用@Mapper
//                            .enableFileOverride()            // 覆盖已经生成的代码
                            .enableBaseResultMap()           // 生成ResultMap
                            .enableBaseColumnList();         // 生成ColumnList
                })
                // ... 其他配置  todo 待验证
                .dataSourceConfig(builder -> {
                    builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                        // 1. 处理 jdbcType 为 TINYINT 的字段
                        if (JdbcType.TINYINT == metaInfo.getJdbcType()) {
                            // 可根据字段名进行更精细的判断，例如只转换 "enable" 字段
                            if ("enable".equals(metaInfo.getColumnName())) {
                                return DbColumnType.BOOLEAN;
                            }
                            // 其他 tinyint 字段你可以选择转成 Integer 或 Byte
                            // return DbColumnType.INTEGER;
                        }
                        // 2. 处理 jdbcType 为 BIT 的特殊情况 (如 tinyint(1) 可能被识别为 BIT)
                        if (JdbcType.BIT == metaInfo.getJdbcType()) {
                            return DbColumnType.BOOLEAN;
                        }
                        // 默认使用 MyBatis-Plus 原有的类型转换规则
                        return typeRegistry.getColumnType(metaInfo);
                    });
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker
                .execute();

    }

}
