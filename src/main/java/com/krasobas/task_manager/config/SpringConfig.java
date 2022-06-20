package com.krasobas.task_manager.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.sql.*;

@Configuration
@ComponentScan("com.krasobas.task_manager")
@PropertySource("classpath:application.properties")
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

//    @Value("${postgresql.driver-class-name}")
//    String driver;
    @Value("${postgresql.url}")
    String url;
//    @Value("${postgresql.username}")
//    String username;
//    @Value("${postgresql.password}")
//    String password;

    @Autowired
    public SpringConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }
    // THYMELEAF
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        SpringTemplateEngine templateEngine = templateEngine();
        templateEngine.addDialect(new Java8TimeDialect());
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine);
        resolver.setCharacterEncoding("UTF-8");
        registry.viewResolver(resolver);
    }
    //Здесь можно назначить статические страницы для нужных url
    //чтобы назначить для стартовой, нужно удалить main.html из webapp каталога
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("tasks/show");
//    }

    // подключаем папку с ресурсами
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean
    public Connection jdbcConnection() {
        Connection db = null;
        try {
            db = dataSource().getConnection();
            if (!checkTable(db, "users")) {
                String sql = "create table users (id serial primary key, login varchar(255), password varchar(255))";
                createTable(db, sql);
            }
            if (!checkTable(db, "tasks")) {
                String sql = "create table tasks (id serial primary key, title varchar(255), content text, created timestamp, done boolean, user_id int references users(id))";
                createTable(db, sql);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return db;
    }

    // подключаем Spring JDBC
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/spring_mvc_db");
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("password");

        dataSource.setUrl(System.getenv("JDBC_DATABASE_URL"));
        dataSource.setUsername(System.getenv("JDBC_DATABASE_USERNAME"));
        dataSource.setPassword(System.getenv("JDBC_DATABASE_PASSWORD"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    private boolean checkTable(Connection db, String table) {
        boolean rsl = false;
        String sql = "select exists (select 1 from information_schema.columns where table_name like ?)";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setString(1, table);
            try (ResultSet rs = ps.executeQuery(sql)) {
                if (rs.next()) {
                    rsl = rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    private void createTable(Connection db, String sql) {
        try (Statement st = db.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
