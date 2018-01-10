package awt.jaxrs.security.authenticator.bearer;

import java.util.Map;

import javax.ws.rs.core.SecurityContext;

import awt.jaxrs.security.AuthorizationHeader;
import awt.jaxrs.security.authenticator.Authenticator;
import io.jsonwebtoken.*;

public abstract class AbstractBearerAuthenticator implements Authenticator {

    @Override
    public SecurityContext authenticate(final AuthorizationHeader header, final Map<String, String[]> parameters) {
	final String token = header.getToken();
	final Jwt<?, ?> jwt = Jwts.parser().setSigningKeyResolver(this.getSigningKeyResolver()).parse(token);
	return this.authenticate(jwt, parameters);
    }

    public abstract SigningKeyResolver getSigningKeyResolver();

    public abstract SecurityContext authenticate(final Jwt<?, ?> jwt, final Map<String, String[]> parameters);
}
