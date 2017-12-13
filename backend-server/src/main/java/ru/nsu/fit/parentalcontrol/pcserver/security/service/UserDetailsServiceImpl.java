package ru.nsu.fit.parentalcontrol.pcserver.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nsu.fit.parentalcontrol.pcserver.model.User;
import ru.nsu.fit.parentalcontrol.pcserver.repository.AdminRepository;
import ru.nsu.fit.parentalcontrol.pcserver.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsServiceWithRoles {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AdminRepository adminRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    final User user = userRepository.findByEmail(email).orElse(null);
    if (user == null)
      throw new UsernameNotFoundException(email);

    final Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    grantedAuthorities.add(USER);
    if (adminRepository.findByUser(user).isPresent())
      grantedAuthorities.add(ADMIN);

    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
  }
}
