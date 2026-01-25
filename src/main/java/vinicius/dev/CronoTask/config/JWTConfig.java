package vinicius.dev.CronoTask.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

@Component
public class JWTConfig
{

    private static final Logger logger = LoggerFactory.getLogger( JWTConfig.class );

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${jwt.refresh.expiration}")
    private long jwtRefreshExpirationMs;

    @Value("${jwt.private.key}")
    private String privateKeyString;

    @Value("${jwt.public.key}")
    private String publicKeyString;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() throws Exception
    {
        // O Java precisa de um "KeyFactory" para reconstruir a chave a partir dos bytes
        // Para EdDSA (Ed25519), usamos este algoritmo:
        KeyFactory keyFactory = KeyFactory.getInstance( "Ed25519" );

        // 1. Decodificar a Chave Privada (Base64 -> Bytes -> Objeto Java)
        byte[] privateKeyBytes = Decoders.BASE64.decode( privateKeyString );
        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec( privateKeyBytes );
        this.privateKey = keyFactory.generatePrivate( privateSpec );

        // 2. Decodificar a Chave Pública
        byte[] publicKeyBytes = Decoders.BASE64.decode( publicKeyString );
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec( publicKeyBytes );
        this.publicKey = keyFactory.generatePublic( publicSpec );
    }

    public String generateToken( String userID )
    {
        return Jwts.builder( )
                .subject( userID )
                .issuedAt( new Date( ) )
                .expiration( new Date( System.currentTimeMillis( ) + jwtExpirationMs ) )
                .signWith( privateKey, Jwts.SIG.EdDSA )
                .compact( );
    }

    public String getUserIDFromToken( String token )
    {
        return Jwts.parser( )
                .verifyWith( publicKey )
                .build( )
                .parseSignedClaims( token )
                .getPayload( )
                .getSubject( );
    }

    public boolean validateJwtToken( String token )
    {
        try
        {
            Jwts.parser( )
                    .verifyWith( publicKey )
                    .build( )
                    .parseSignedClaims( token );
            return true;
        } catch (ExpiredJwtException e)
        {
            logger.warn( "Token JWT expirado: {}", e.getMessage( ) );
        } catch (JwtException | IllegalArgumentException e)
        {
            logger.error( "Token JWT inválido: {}", e.getMessage( ) );
        }
        return false;
    }
}