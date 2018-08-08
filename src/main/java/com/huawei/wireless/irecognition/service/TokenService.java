package com.huawei.wireless.irecognition.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.impl.TextCodec.BASE64;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;


@Service
public class TokenService implements ITokenService {

    private static final String DOT = ".";
    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    private String issuer;
    private int expirationSec;
    private int clockSkewSec;
    private String secretKey;

    public TokenService(@Value("${jwt.issuer:akif}") final String issuer,
                            @Value("${jwt.expiration-sec:86400}") final int expirationSec,
                            @Value("${jwt.clock-skew-sec:300}") final int clockSkewSec,
                            @Value("${jwt.secret:secret}") final String secret) {
        super();
        this.issuer = requireNonNull(issuer);
        this.expirationSec = requireNonNull(expirationSec);
        this.clockSkewSec = requireNonNull(clockSkewSec);
        this.secretKey = BASE64.encode(requireNonNull(secret));
    }

    @Override
    public String permanent(final Map<String, String> attributes) {
        return newToken(attributes, 0);
    }

    @Override
    public String expiring(final Map<String, String> attributes) {
        return newToken(attributes, expirationSec);
    }

    private String newToken(final Map<String, String> attributes, final int expiresInSec) {
        final Date now = new Date();
        final Claims claims = Jwts
                .claims()
                .setIssuer(issuer)
                .setIssuedAt(now);

        if (expiresInSec > 0) {
            final Date expiresAt = new Date(now.getTime() + expirationSec * 1000);
            claims.setExpiration(expiresAt);
        }
        claims.putAll(attributes);

        String token = Jwts
                .builder()
                .setClaims(claims)
                .signWith(HS256, secretKey)
                .compressWith(COMPRESSION_CODEC)
                .compact();

        System.out.println("JWT is " + token);

        return token;
    }

    @Override
    public Map<String, String> verify(final String token) {
        final JwtParser parser = Jwts
                .parser()
                .requireIssuer(issuer)
                .setClock(Date::new)
                .setAllowedClockSkewSeconds(clockSkewSec)
                .setSigningKey(secretKey);

        return parseClaims(parser.parseClaimsJws(token).getBody());
    }

    @Override
    public Map<String, String> untrusted(final String token) {
        final JwtParser parser = Jwts
                .parser()
                .requireIssuer(issuer)
                .setClock(Date::new)
                .setAllowedClockSkewSeconds(clockSkewSec);

        // See: https://github.com/jwtk/jjwt/issues/135
        final String withoutSignature = substringBeforeLast(token, DOT) + DOT;
        return parseClaims( parser.parseClaimsJwt(withoutSignature).getBody());
    }

    private static Map<String, String> parseClaims(final Claims claims) {

        final Map<String, String> map = new HashMap<>();
        for (final Map.Entry<String, Object> e: claims.entrySet()) {
            map.put(e.getKey(), String.valueOf(e.getValue()));
        }

        return map;
    }

}

