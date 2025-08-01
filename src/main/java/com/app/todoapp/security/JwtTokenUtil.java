package com.app.todoapp.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.DefaultClock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.app.todoapp.util.CryptoAES128;
import com.app.todoapp.util.TokenSecure;

@Component
public class JwtTokenUtil implements Serializable {

	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_CREATED = "iat";
	
	static final String OS_INFO = "clientOsInfo";
	static final String BROWSER_INFO = "clientBrowserInfo";
	
	private static final long serialVersionUID = -3301605591108950415L;

	private Clock clock = DefaultClock.INSTANCE;

	@Value("${security.jwt.secret}")
	private String secret;

	@Value("${security.jwt.expiration}")
	private Long expiration;
	
	@Autowired
	private CryptoAES128 cryptoAES128;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public String getEmailFromToken(String token) {
		System.out.println(token);
		token=cryptoAES128.decrypt(token);
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	public String getIdFromToken(String token) {
		token=cryptoAES128.decrypt(token);
		return getClaimFromToken(token, Claims::getId);
	}
	
	public String getOSInfoFromToken(String token,int type) {
		return getOSAndBrowserInfoFromToken(token,type);
	}
	
	public String getUserIdFromToken(String token) {
		return getClaimFromToken(token, Claims::getId);
	}
	
	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(clock.now());
	}

	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return false;
		// System.out.println(">>>>>>>>>>>>>>>>>>"+created.toLocaleString()+"
		// "+lastPasswordReset);
		// return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	private Boolean ignoreTokenExpiration(String token) {
		return false;
	}

	public String generateToken(JwtUser userDetails,HttpServletRequest request) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(OS_INFO, TokenSecure.getOSInfo());
		claims.put(BROWSER_INFO, TokenSecure.getBrowserInfo(request));
		return doGenerateToken(claims, userDetails);
	}

	public String pbGenerateToken(UserDetails userDetails, int experyDate) {
		Map<String, Object> claims = new HashMap<>();
		return pbDoGenerateToken(claims, userDetails.getUsername(), experyDate);
	}
	

	private String getOSAndBrowserInfoFromToken(String token,int type) {
		String os=String.valueOf(getAllClaimsFromToken(token).get(OS_INFO));
		String browser=String.valueOf(getAllClaimsFromToken(token).get(BROWSER_INFO));
		return type==1?os:browser;
	}


	private String doGenerateToken(Map<String, Object> claims, JwtUser userDetails) {
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate);
		return Jwts.builder().setClaims(claims).setId(String.valueOf(userDetails.getId())).setSubject(userDetails.getUsername()).setIssuedAt(createdDate)
				.setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	private String pbDoGenerateToken(Map<String, Object> claims, String subject, int experyDate) {
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDates(createdDate, experyDate);

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(createdDate)
				.setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = getIssuedAtDateFromToken(token);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
				&& (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public String refreshToken(String token) {
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate);

		final Claims claims = getAllClaimsFromToken(token);
		claims.setIssuedAt(createdDate);
		claims.setExpiration(expirationDate);

		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		JwtUser user = (JwtUser) userDetails;
		final String username = getUsernameFromToken(token);
		final Date created = getIssuedAtDateFromToken(token);
		// final Date expiration = getExpirationDateFromToken(token);
		return (username.equals(user.getUsername()) && !isTokenExpired(token)
				&& !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
	}

	public int InValidOrValidateToken(String token, UserDetails userDetails) {
		JwtUser user = (JwtUser) userDetails;
		try {
				final String username = getUsernameFromToken(token);
				final Date created = getIssuedAtDateFromToken(token);
				// final Date expiration = getExpirationDateFromToken(token);
				return (username.equals(user.getUsername()) && !isTokenExpired(token)
						&& !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())) ? (1) : (0);
		} catch (ExpiredJwtException e) {
			return 2;
			
		} catch (SignatureException e) {
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	private Date calculateExpirationDate(Date createdDate) {
		return new Date(createdDate.getTime() + expiration * 1000);
	}

	private Date calculateExpirationDates(Date createdDate, int expiration) {
		return new Date(createdDate.getTime() + expiration * 1000 * 60 * 60);
	}

}
