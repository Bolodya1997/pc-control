package ru.nsu.fit.parentalcontrol.pcserver.security.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RepositoryConfig {

  @Bean
  @Primary
  public AuthRepository authRepository() {
    return new AuthRepositoryMock();
  }

  @Bean
  @Primary
  public AdminRepository adminRepository() {
    return new AdminRepositoryMock();
  }
}
