package ro.ne8.resourceserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;


@Configuration
@EnableResourceServer
public class SecurityResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private Environment env;

    //

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests().anyRequest().permitAll();
        // @formatter:on
    }

    @Override
    public void configure(final ResourceServerSecurityConfigurer config) {
        config.tokenServices(this.tokenServices());
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(this.tokenStore());
        return defaultTokenServices;
    }

    // JDBC token store configuration
    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(this.env.getProperty("spring.datasource.oauth2.driver-class-name"));
        dataSource.setUrl(this.env.getProperty("spring.datasource.oauth2.url"));
        dataSource.setUsername(this.env.getProperty("spring.datasource.oauth2.username"));
        dataSource.setPassword(this.env.getProperty("spring.datasource.oauth2.password"));
        return dataSource;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(this.dataSource());
    }
}
