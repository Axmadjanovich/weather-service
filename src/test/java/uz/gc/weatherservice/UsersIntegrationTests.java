package uz.gc.weatherservice;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import uz.gc.weatherservice.dto.CityDto;
import uz.gc.weatherservice.dto.LoginDto;
import uz.gc.weatherservice.dto.ResponseDto;
import uz.gc.weatherservice.dto.UsersDto;
import uz.gc.weatherservice.model.Users;
import uz.gc.weatherservice.repository.UsersRepository;
import uz.gc.weatherservice.service.CityService;
import uz.gc.weatherservice.service.UserService;
import uz.gc.weatherservice.service.impl.UsersServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static reactor.core.publisher.Mono.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@DirtiesContext
@Slf4j
@Nested
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsersIntegrationTests {

    private WebTestClient webTestClient;
    private static String token;

    @Value("${test.email}")
    private String email;
    @Value("${test.password}")
    private String password;

    @Autowired
    private UsersRepository usersRepository;

    @MockBean
    private CityService cityService;

    @LocalServerPort
    private int randomServerPort;

    @BeforeEach
    public void setup() {
        this.webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + randomServerPort).build();
    }

    @Test
    @Order(2)
//    @Sql(value = "/db/mock/insert_mock_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void add_new_user(){
//        usersRepository.deleteAll()
//                .block();

        UsersDto testUser = UsersDto.builder()
                .id(1)
                .firstName("test")
                .lastName("test")
                .email(email)
                .password(password)
                .phoneNumber("+998901234567")
                .build();

        WebTestClient.ResponseSpec responseSpec = webTestClient.post()
                .uri("/users/sign-up")
                .bodyValue(testUser)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json");

        responseSpec.expectBody(new ParameterizedTypeReference<ResponseDto<Users>>() {})
                .value(r -> {
                    assertEquals(r.getCode(), 0);
                    assertNotNull(r.getData());
                    assertEquals(r.getData().getEmail(), testUser.getEmail());
                });

    }

    @Order(3)
    @Test
    public void get_token_with_email_and_password(){
        ResponseDto<String> response = webTestClient.post()
                .uri("/users/register")
                .bodyValue(LoginDto.builder()
                        .email(email)
                        .password(password)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .returnResult(new ParameterizedTypeReference<ResponseDto<String>>() {
                })
                .getResponseBody()
                .doOnNext(res -> log.info("Response: {}", res))
                .blockFirst();

        assertNotNull(response, "Response is null");
        assertNotNull(response.getData(), "Token is not retrieved");

        token = response.getData();

        assertEquals(response.getCode(), 0, "Unexpected response code");
    }
}
