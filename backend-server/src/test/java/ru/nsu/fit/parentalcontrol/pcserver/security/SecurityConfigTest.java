package ru.nsu.fit.parentalcontrol.pcserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.nsu.fit.parentalcontrol.pcserver.security.model.Auth;
import ru.nsu.fit.parentalcontrol.pcserver.security.repository.MockUsers;
import ru.nsu.fit.parentalcontrol.pcserver.security.repository.RepositoryConfig;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@WebAppConfiguration
//@ContextConfiguration(classes = { RepositoryConfig.class })
public class SecurityConfigTest {

//  TODO: (Volodya): finish these tests

//  @Autowired
  private WebApplicationContext wac;

//  @Autowired
  private FilterChainProxy filterChain;

  private MockMvc mvc;

//  @Before
  public void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(wac)
        .addFilter(filterChain)
        .build();
  }

//  @Test
  public void loginCorrectTest() throws Exception {
    final ObjectMapper mapper = new ObjectMapper();
    final Auth auth = new Auth(MockUsers.USER_EMAIL, MockUsers.PASSWORD);

    mvc.perform(post("/rest/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(auth)))
        .andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.LOCATION,
            "/rest/user?email=" + MockUsers.USER_EMAIL));
  }

//  @Test
  public void loginIncorrectTest() {
    fail();
  }
}