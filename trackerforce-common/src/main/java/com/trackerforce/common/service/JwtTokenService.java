package com.trackerforce.common.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.trackerforce.common.model.JwtPayload;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenService {

	@Value("${service.jwt.expire}")
	private int JWT_TOKEN_VALIDITY;

	@Value("${service.jwt.secret}")
	private String secret;
	
	public Jws<Claims> getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(secret.getBytes()).build().parseClaimsJws(token);
	}
	
	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(secret.getBytes())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public JwtPayload readClaims(String token) {
		Base64.Decoder decoder = Base64.getDecoder();
		String[] chunks = token.split("\\.");
		final var payload = new String(decoder.decode(chunks[1]));
		
		Gson gson = new Gson();
		return gson.fromJson(payload, JwtPayload.class);
	}
	
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	private String generateRefreshToken(String token) {
		return new BCryptPasswordEncoder().encode(token.split("\\.")[2]);
	}
	
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	public String[] generateToken(String subject, String organizationAlias, Map<String, Object> claims) {
		final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		final String token = Jwts.builder()
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 60 * 1000))
				.setSubject(subject)
				.setAudience(organizationAlias)
				.addClaims(claims)
				.signWith(key)
				.compact();
		
		return new String[] { token, generateRefreshToken(token) };
	}
	
	public boolean isRefreshTokenValid(String token, String refreshToken) {
		return new BCryptPasswordEncoder().matches(token.split("\\.")[2], refreshToken);
	}
	
	public Boolean validateToken(String token, String subject) {
		final String username = getUsernameFromToken(token);
		return (username.equals(subject) && !isTokenExpired(token));
	}
}