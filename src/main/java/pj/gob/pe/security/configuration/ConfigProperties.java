package pj.gob.pe.security.configuration;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ConfigProperties {

    @Value("${api.authorization.url}")
    private String urlAuthorization;

    @Value("${api.authorization.login.path}")
    private String pathLogin;

    @Value("${api.authorization.introspect.path}")
    private String pathIntrospectToken;

    @Value("${api.authorization.logout.path}")
    private String pathLogout;

    @Value("${spring.data.redis.prefix:jurisia_security}")
    private String REDIS_KEY_PREFIX;

    @Value("${spring.data.redis.ttl:3600}")
    private long REDIS_TTL;

    @Value("${api.judicial.url}")
    private String urlConsultaSIJ;

    @Value("${api.judicial.get.user.path}")
    private String ConsultaUsuarioPath;
}
