package com.enterprise.services;




import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
//import java.util.function.Function;
//import java.security.NoSuchAlgorithmException;
//import java.util.Base64;
//import javax.crypto.KeyGenerator;/
import java.security.Key;
import javax.crypto.SecretKey;

import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import com.enterprise.models.Users;





@Service
public class JWTService {
	
	@Value("${jwt.secret}")
	private String SECRET_KEY;
	
//	public JWTService() {
//		
//		try {
//			KeyGenerator keygen = KeyGenerator.getInstance("HmacSHA256");
//			SecretKey sk = keygen.generateKey();
//			this.SECRET_KEY = Base64.getEncoder().encodeToString(sk.getEncoded());
//			System.out.println("Secret Key : "+SECRET_KEY);
//			
//		}
//		catch(NoSuchAlgorithmException e) {
//			throw new RuntimeException(e);
//		}
//	}
	
	public String generateToken(Users user) {
		long expirationMillis = 1000 * 60 * 60; // 1 hour

		Date now = new Date();
		Date expiry = new Date(now.getTime() + expirationMillis);
		String jwtToken = Jwts
				          .builder()
				          .subject(user.getUsername())
				          .claim("email",user.getEmail())
				          .claim("roles", user.getRoles())
				          .issuedAt(now)
				          .signWith(getKey())
				          .expiration(expiry)
				          .compact();
		return jwtToken;
	}
	
	public Key getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		Key key = Keys.hmacShaKeyFor(keyBytes);
		return key;
	}

	public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
        // using this token we have to extract username username ~ subject by convention
        // Claims having getSubject method  we are passing its method reference 
        
    }

    // ✅ 2. Validate Token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        // verifies extracted username with userDetails and token is expired or not 
    }

    // --- Helper methods ---

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
        // checks if extracted date is before the current date 
    }

    private Date extractExpiration(String token) {
    	    
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    	    //Function<Claims, T> claimsResolver   is functional interface can hold method reference 
    	    // function accepts Claims object and returns based on method 
    	    
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
        // this will execute the method it holds 
    }
    
    private Claims extractAllClaims(String token) {
    	    Jwt<?,Claims> jwt = Jwts
    	    		                .parser() // returns jwts parser builder object
    	    		                .verifyWith((SecretKey) getKey()) // builder object includes parser 
    	    		                .build() // now a parser is built
    	    		                .parseSignedClaims(token); // payload (claims) extracted after verifying signature 
    	                         // with secret key
    	    return jwt.getPayload();  // returnning payload ~ Claims 
    	    		                
    	    		                
    }
	
}
