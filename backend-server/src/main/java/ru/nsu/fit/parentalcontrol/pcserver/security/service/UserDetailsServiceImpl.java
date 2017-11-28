package ru.nsu.fit.parentalcontrol.pcserver.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nsu.fit.parentalcontrol.pcserver.security.model.Auth;
import ru.nsu.fit.parentalcontrol.pcserver.security.repository.AdminRepository;
import ru.nsu.fit.parentalcontrol.pcserver.security.repository.AuthRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private AuthRepository authRepository;

  @Autowired
  private AdminRepository adminRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    final Auth auth = authRepository.findOne(email);

    final Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
    if (adminRepository.findOne(auth.getEmail()) != null)
      grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));

    return new User(auth.getEmail(), auth.getPassword(), grantedAuthorities);
  }
}
