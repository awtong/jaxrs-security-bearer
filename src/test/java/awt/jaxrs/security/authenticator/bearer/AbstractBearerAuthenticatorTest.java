package awt.jaxrs.security.authenticator.bearer;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.security.*;
import java.util.*;

import org.junit.*;
import org.mockito.*;

import awt.jaxrs.security.AuthorizationHeader;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureException;

public class AbstractBearerAuthenticatorTest {
    private static final KeyPairGenerator KEYPAIR_GENERATOR;

    static {
	try {
	    KEYPAIR_GENERATOR = KeyPairGenerator.getInstance("RSA");
	    KEYPAIR_GENERATOR.initialize(2048);
	} catch (final NoSuchAlgorithmException exception) {
	    throw new ExceptionInInitializerError(exception);
	}
    }

    private KeyPair pair;

    @Mock
    private SigningKeyResolverAdapter resolver;

    @SuppressWarnings("unchecked")
    @Test
    public void testAuthenticate() {
	final AbstractBearerAuthenticator authenticator = mock(AbstractBearerAuthenticator.class);
	when(this.resolver.resolveSigningKey(any(JwsHeader.class), any(Claims.class)))
	.thenReturn(this.pair.getPublic());
	when(authenticator.getSigningKeyResolver()).thenReturn(this.resolver);
	when(authenticator.authenticate(any(AuthorizationHeader.class), any(Map.class))).thenCallRealMethod();

	final String jwt = Jwts.builder().claim("name", "test-name")
		.signWith(SignatureAlgorithm.RS512, this.pair.getPrivate()).compact();
	final AuthorizationHeader header = mock(AuthorizationHeader.class);
	when(header.getToken()).thenReturn(jwt);

	authenticator.authenticate(header, Collections.emptyMap());
	verify(authenticator, times(1)).authenticate(any(Jwt.class), any(Map.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAuthenticateMismatchedKeyPair() {
	final KeyPair nonmatching = KEYPAIR_GENERATOR.generateKeyPair();
	final AbstractBearerAuthenticator authenticator = mock(AbstractBearerAuthenticator.class);
	when(this.resolver.resolveSigningKey(any(JwsHeader.class), any(Claims.class)))
	.thenReturn(nonmatching.getPublic());
	when(authenticator.getSigningKeyResolver()).thenReturn(this.resolver);
	when(authenticator.authenticate(any(AuthorizationHeader.class), any(Map.class))).thenCallRealMethod();

	final String jwt = Jwts.builder().claim("name", "test-name")
		.signWith(SignatureAlgorithm.RS512, this.pair.getPrivate()).compact();
	final AuthorizationHeader header = mock(AuthorizationHeader.class);
	when(header.getToken()).thenReturn(jwt);

	try {
	    authenticator.authenticate(header, Collections.emptyMap());
	    fail("Should throw SignatureException");
	} catch (final SignatureException exception) {

	}
    }

    @Before
    public void setUp() {
	MockitoAnnotations.initMocks(this);
	this.pair = KEYPAIR_GENERATOR.generateKeyPair();
    }
}
