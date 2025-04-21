package pj.gob.pe.security.service.externals;

import pj.gob.pe.security.model.beans.ResponseLogin;
import pj.gob.pe.security.model.beans.TokenResponse;
import pj.gob.pe.security.utils.beans.LoginInput;
import pj.gob.pe.security.utils.beans.LogoutInput;
import pj.gob.pe.security.utils.beans.VerifySessionInput;

public interface AuthService {

    public TokenResponse authenticate(LoginInput login) throws Exception;

    public ResponseLogin generateSessionId(String username, TokenResponse token, String clientIp) throws Exception;

    public ResponseLogin sesionData(String userSessionsId) throws Exception;

    public ResponseLogin verifySession(VerifySessionInput verifySession) throws Exception;

    public void logout(LogoutInput logout);
}
