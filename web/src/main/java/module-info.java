module web {
    // ------------------------- spring-boot-starter --------------------------
    // @SpringBootApplication
    requires spring.boot.autoconfigure;

    // SpringApplication
    requires spring.boot;

    // SQLException
    requires java.sql;

    // 使用反射需要 opens
    opens xianzhan;
    // ------------------------- spring-boot-starter --------------------------


    // ----------------------- spring-boot-starter-web ------------------------
    // @Controller
    requires spring.context;
    // @RequestMapping, @ResponseBody
    requires spring.web;
    // @JsonIgnore, @JsonFormat, @JsonInclude
    requires jackson.annotations;
    // web
    exports xianzhan.controller;
    exports xianzhan.pojo.vo;
    // ----------------------- spring-boot-starter-web ------------------------
}