package pj.gob.pe.security.dao.redis.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import pj.gob.pe.security.configuration.ConfigProperties;
import pj.gob.pe.security.dao.redis.UserLoginRedisDao;
import pj.gob.pe.security.model.beans.UserLogin;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class UserLoginRedisDaoImpl implements UserLoginRedisDao {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ConfigProperties properties;

    @Override
    public void saveUserSession(String key, UserLogin value) {
        String prefixedKey = getPrefixedKey(key);
        redisTemplate.opsForValue().set(prefixedKey, value, properties.getREDIS_TTL(), TimeUnit.SECONDS);
    }

    @Override
    public UserLogin getUserSession(String key) {
        String prefixedKey = getPrefixedKey(key);
        Object response = redisTemplate.opsForValue().get(prefixedKey);

        if (response == null) return null; // Retornar null si no hay datos en Redis

        // Convertir el LinkedHashMap en un objeto UserLogin
        return objectMapper.convertValue(response, UserLogin.class);
    }

    @Override
    public void deleteUserSession(String key) {
        String prefixedKey = getPrefixedKey(key);
        redisTemplate.delete(prefixedKey);
    }

    // Método para agregar prefijo a las claves
    private String getPrefixedKey(String key) {
        return properties.getREDIS_KEY_PREFIX() + "_" + key;
    }

}
