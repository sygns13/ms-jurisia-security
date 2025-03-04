package pj.gob.pe.security.dao.redis;

import pj.gob.pe.security.model.beans.UserLogin;

public interface UserLoginRedisDao {

    public void saveUserSession(String key, UserLogin value);

    public UserLogin getUserSession(String key);

    public void deleteUserSession(String key);
}
