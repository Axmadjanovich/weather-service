package uz.gc.weatherservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import uz.gc.weatherservice.dto.CityDto;
import uz.gc.weatherservice.dto.LoginDto;
import uz.gc.weatherservice.dto.ResponseDto;
import uz.gc.weatherservice.dto.UsersDto;
import uz.gc.weatherservice.model.Roles;
import uz.gc.weatherservice.model.Users;
import uz.gc.weatherservice.repository.UserAuthoritiesRepository;
import uz.gc.weatherservice.service.CityService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@DirtiesContext
@Slf4j
@Sql(scripts = "/db/mock/insert_mock_users.sql", config = @SqlConfig(dataSource = "postgres"))
@Nested
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CitiesIntegrationTest {

    private WebTestClient webTestClient;
    private static String token;

    @Value("${admin.email}")
    private String email;
    @Value("${admin.password}")
    private String password;

    @MockBean
    private CityService cityService;

    @Autowired
    private UserAuthoritiesRepository UARepo;

    @LocalServerPort
    private int randomServerPort;

    @BeforeEach
    public void setup() {
        this.webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + randomServerPort).build();
    }

    @Test
    @Order(2)
    public void add_new_user(){

        UsersDto testUser = UsersDto.builder()
                .id(2)
                .firstName("test")
                .lastName("test")
                .email(email)
                .password(password)
                .phoneNumber("+998901234560")
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
                    UARepo.save(new Roles(r.getData().getId(), 2));
                });
    }


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
                .returnResult(new ParameterizedTypeReference<ResponseDto<String>>() {})
                .getResponseBody()
                .doOnNext(res -> log.info("Response: {}", res))
                .blockFirst();

        assertNotNull(response, "Response is null");
        assertNotNull(response.getData(), "Token is not retrieved");

        token = response.getData();

        assertEquals(response.getCode(), 0, "Unexpected response code");
    }

    @Test
    public void add_new_cities(){
        CityDto tashkent = CityDto.builder()
                .id(1)
                .name("Tashkent")
                .shortName("TAS")
                .country("Uzbekistan")
                .build();

        Flux<ResponseDto<CityDto>> response = webTestClient.post()
                .uri("/cities")
                .bodyValue(tashkent)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .returnResult(new ParameterizedTypeReference<ResponseDto<CityDto>>() {
                }).getResponseBody();

        StepVerifier.create(response)
                .expectSubscription()
                .expectNext(ResponseDto.<CityDto>builder()
                        .data(CityDto.builder()
                                .id(1)
                                .name("Tashkent")
                                .shortName("TAS")
                                .country("Uzbekistan")
                                .build())
                        .message("OK")
                        .success(true)
                        .build())
                .verifyComplete();
    }

    @Test
    public void get_cities_list_with_authorization(){
        Flux<CityDto> response = webTestClient.get()
                .uri("/cities/cities-list")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .returnResult(CityDto.class)
                .getResponseBody();

        StepVerifier.create(response)
                .expectSubscription()
                .expectNext(CityDto.builder()
                        .id(1)
                        .name("Tashkent")
                        .shortName("TAS")
                        .country("Uzbekistan")
                        .build())
                .verifyComplete();

    }
}
