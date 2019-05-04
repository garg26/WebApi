package com.neostencil.security;

import com.neostencil.model.entities.Authority;
import com.neostencil.model.entities.User;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public final class JwtUserFactory {

  private JwtUserFactory() {}

  public static JwtUser create(User user) {
    return new JwtUser(user.getUserId(), user.getUserName(), user.getFirstname(),
        user.getLastname(), user.getFullName(), user.getEmailId(), user.getPassword(),
        mapToGrantedAuthorities(user.getAuthorities()), user.getEnabled(),
        user.getLastPasswordResetDate(), user.getExamSegment(),user.getCity(),user.getMobileNumber());
  }

  private static List<GrantedAuthority> mapToGrantedAuthorities(Set<Authority> authorities) {
    return authorities.stream()
        .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
        .collect(Collectors.toList());
  }
}
