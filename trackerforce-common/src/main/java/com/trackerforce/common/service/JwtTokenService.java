package com.trackerforce.common.service;

import com.google.gson.Gson;
import com.trackerforce.common.model.JwtPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenService {

	private final int jwtTokenExpire;

	private final String jwtSecret;

	public JwtTokenService(
			@Value("${service.jwt.expire}") int jwtTokenExpire,
			@Value("${service.jwt.secret}") String jwtSecret) {
		this.jwtTokenExpire = jwtTokenExpire * 60 * 1000;
		this.jwtSecret = jwtSecret;
	}

	public Claims getAllClaimsFromToken(String token) {
		final var key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
		final var jwtParser = Jwts.parser().verifyWith(key).build();
		return jwtParser.parseSignedClaims(token).getPayload();
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
		var rawToken = new BCryptPasswordEncoder().encode(token.split("\\.")[2]);
		return Base64.getEncoder().encodeToString(rawToken.getBytes());
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

	public Pair<String, String> generateToken(String subject, List<String> organizationAlias, Map<String, Object> claims) {
		final var key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
		final var claimsBuild = Jwts.claims()
				.expiration(new Date(System.currentTimeMillis() + jwtTokenExpire))
				.audience().add(organizationAlias)
				.and()
				.add(claims)
				.subject(subject).build();

		final var token = Jwts.builder().claims(claimsBuild).signWith(key).compact();

		return Pair.of(token, generateRefreshToken(token));
	}
	
	public boolean isRefreshTokenValid(String token, String refreshToken) {
		var decodedRefreshToken = Base64.getDecoder().decode(refreshToken);
		return new BCryptPasswordEncoder().matches(token.split("\\.")[2], new String(decodedRefreshToken));
	}
	
	public Boolean validateToken(String token, String subject) {
		final String username = getUsernameFromToken(token);
		return (username.equals(subject) && !isTokenExpired(token));
	}
}