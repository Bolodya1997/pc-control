package ru.nsu.fit.parentalcontrol.pcserver.security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDetailsServiceWithRoles extends UserDetailsService {

  GrantedAuthority USER = new SimpleGrantedAuthority("ROLE_USER");
  GrantedAuthority ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
}
