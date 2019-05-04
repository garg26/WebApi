package com.neostencil.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.neostencil.model.entities.Authority;
import com.neostencil.model.entities.AuthorityName;
import com.neostencil.model.entities.User;
import com.neostencil.model.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;

@Component
public class JwtTokenUtil implements Serializable {

  private static final String CLAIM_KEY_USERNAME = "sub";
  private static final String CLAIM_KEY_ID = "id";
  private static final String CLAIM_KEY_NAME = "name";
  private static final String CLAIM_KEY_ROLE = "role";
  private static final String CLAIM_KEY_EXAMS = "exams";
  private static final String CLAIM_KEY_CITY = "city";
  private static final String CLAIM_KEY_PHONE_NUMBER = "phonenumber";

  private static final String CLAIM_KEY_CREATED = "created";
  private static final String CLAIM_KEY_EMAIL_ID = "emailId";

  private static final String CLAIM_KEY_PASSWORDHASH = "emailId";

  private static final long serialVersionUID = -3301605591108950415L;
  // @SuppressFBWarnings(value = "SE_BAD_FIELD", justification = "It's okay here")
  private Clock clock = DefaultClock.INSTANCE;

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  @Autowired
  private UserRepository userRepository;

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
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

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(clock.now());
  }

  private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
    return (lastPasswordReset != null && created.before(lastPasswordReset));
  }

  private Boolean ignoreTokenExpiration(String token) {
    // here you specify tokens, for that the expiration is ignored
    return false;
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    JwtUser jwtUser = (JwtUser) userDetails;
    if (jwtUser != null) {
      claims.put(CLAIM_KEY_ID, jwtUser.getId());
      claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
      claims.put(CLAIM_KEY_NAME, ((JwtUser) userDetails).getFullName());
      claims.put(CLAIM_KEY_ROLE, userDetails.getAuthorities());
      claims.put(CLAIM_KEY_CREATED, new Date());
      claims.put(CLAIM_KEY_EXAMS, ((JwtUser) userDetails).getExamSegments());
      claims.put(CLAIM_KEY_CITY, ((JwtUser) userDetails).getCity());
      claims.put(CLAIM_KEY_PHONE_NUMBER,((JwtUser) userDetails).getPhoneNumber());
      claims.put(CLAIM_KEY_EMAIL_ID, jwtUser.getEmail());
    }
    return doGenerateToken(claims, userDetails.getUsername());
  }

  public String generatePasswordResetToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    JwtUser jwtUser = (JwtUser) userDetails;
    if (jwtUser != null) {
      claims.put(CLAIM_KEY_ID, jwtUser.getId());
      claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
      claims.put(CLAIM_KEY_EMAIL_ID, jwtUser.getEmail());
      claims.put(CLAIM_KEY_PASSWORDHASH,
          userDetails.getPassword() + "-" + ((JwtUser) userDetails).getLastPasswordResetDate());
    }
    return doGenerateToken(claims, userDetails.getUsername());
  }

  private String doGenerateToken(Map<String, Object> claims, String subject) {
    final Date createdDate = clock.now();
    final Date expirationDate = calculateExpirationDate(createdDate);

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

  public String getLoggedInUserName() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    JwtUser loggedInUser = (JwtUser) (auth.getPrincipal());
    return loggedInUser.getUsername();
  }

  public long getLoggedInUserID() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if(auth.getPrincipal().getClass()==String.class){
      return 0;
    }
    JwtUser loggedInUser = (JwtUser) (auth.getPrincipal());
    return loggedInUser.getId();
  }

  public Set<AuthorityName> getLoggedInUserRole() {
    Set<AuthorityName> roles = new HashSet<AuthorityName>();
    long userId = this.getLoggedInUserID();
    if (userId != 0) {
      User user = userRepository.findByUserId(userId);
      if (user != null && user.getAuthorities() != null && user.getAuthorities().size() > 0) {
        for(Authority authority:user.getAuthorities())
        {
          if(authority!=null && authority.getName()!=null)
          {
            roles.add(authority.getName());
          }
        }
      }
    }


    return roles;
  }

  public String getLoggedInUserEmail() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    JwtUser loggedInUser = (JwtUser) (auth.getPrincipal());
    return loggedInUser.getEmail();
  }

  private Date calculateExpirationDate(Date createdDate) {
    return new Date(createdDate.getTime() + expiration * 1000);
  }

  public String getPasswordHashFromToken(String token) {
    Claims allClaims = getAllClaimsFromToken(token);
    String rv = allClaims.get(CLAIM_KEY_PASSWORDHASH, String.class);
    return rv;
  }

  public boolean validateResetToken(String token, UserDetails userDetails) {
    JwtUser user = (JwtUser) userDetails;
    final String username = getUsernameFromToken(token);
    final Date created = getIssuedAtDateFromToken(token);
    String getPasswordHash = getPasswordHashFromToken(token);
    String storedExpectedHash =
        userDetails.getPassword() + "-" + ((JwtUser) userDetails).getLastPasswordResetDate();

    // final Date expiration = getExpirationDateFromToken(token);
    return (username.equals(user.getUsername()) && !isTokenExpired(token)
        && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())
        && (getPasswordHash.equals(storedExpectedHash)));
  }
}
