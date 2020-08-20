package com;


import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.taskuser.TaskUserApplication;
import com.taskuser.dto.UserUpdateDto;
import com.taskuser.model.User;
import com.taskuser.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TaskUserApplication.class)
public class UserControllerIT {

    @Value("${local.server.port}")
    protected int serverPort;

    @Autowired
    UserRepository userRepository;

    private String firstName1 = "smith";
    private String firstName2 = "cliff";
    private String lastName1 = "john";
    private String lastName2 = "michel";
    private String userName1 = "jsmith";
    private String userName2 = "cmich";
    private String FIRST_NAME_FIELD = "first_name";
    private String LAST_NAME_FIELD = "last_name";
    private String USER_NAME_FIELD = "user_name";

    @Before
    public void setUp() {

        RestAssured.port = serverPort;
        userRepository.deleteAll();

    }

    @Test
    public void canCreateUserSuccessfully() {
        User user1 = new User(userName1, firstName1, lastName1);

        given()
                .body(user1).contentType(ContentType.JSON)
                .when()
                .post("/api/user/")
                .then()
                .statusCode(equalTo(HttpStatus.OK.value()))
                .body(FIRST_NAME_FIELD, is(firstName1))
                .body(LAST_NAME_FIELD, is(lastName1))
                .body(USER_NAME_FIELD, is(userName1));
    }

    @Test
    public void shouldReturn400WhenOneOrMoreFieldsAbsent() {
        User user = new User(null, null, null);

        given()
                .contentType(ContentType.JSON)
                .body(user).contentType(ContentType.JSON)
                .when()
                .post("/api/user/")
                .then()
                .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()));

    }

    @Test
    public void shouldUpdateUserInfoSuccessfully() {
        User user1 = new User(userName1, firstName1, lastName1);
        User savedUser = userRepository.save(user1);
        Long userId = savedUser.getId();

        UserUpdateDto userUpdateDto = new UserUpdateDto(firstName2, lastName2);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(userUpdateDto).contentType(ContentType.JSON)
                .when()
                .put("/api/user/" + userId)
                .then()
                .body(FIRST_NAME_FIELD, Matchers.is(firstName2))
                .body(LAST_NAME_FIELD, Matchers.is(lastName2));
    }


    @Test
    public void shouldUpdateFirstNameOnly() {
        User user1 = new User(userName1, firstName1, lastName1);
        User savedUser = userRepository.save(user1);
        Long userId = savedUser.getId();

        UserUpdateDto userUpdateDto = new UserUpdateDto(firstName2, null);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(userUpdateDto).contentType(ContentType.JSON)
                .when()
                .put("/api/user/" + userId)
                .then()
                .body(FIRST_NAME_FIELD, Matchers.is(firstName2))
                .body(USER_NAME_FIELD, Matchers.is(userName1))
                .body(LAST_NAME_FIELD, Matchers.is(lastName1));


    }

    @Test
    public void shouldReturn404_whenUpdatingNonexistentUser() {
        int userId = 100000;

        UserUpdateDto userUpdateDto = new UserUpdateDto(firstName2, lastName2);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(userUpdateDto).contentType(ContentType.JSON)
                .when()
                .put("/api/user/" + userId)
                .then()
                .statusCode(equalTo(HttpStatus.NOT_FOUND.value()))
                .body(containsString("User with id " + userId + " doesn't exist"));
    }

    @Test
    public void testIfDuplicateUserBeforeSaving() {
        User user1 = new User(userName1, firstName1, lastName1);
        User user1Saved = userRepository.save(user1);
        User user2 = new User(userName1, firstName2, lastName2);

        given()
                .body(user2).contentType(ContentType.JSON)
                .when()
                .post("/api/user/")
                .then()
                .statusCode(equalTo(HttpStatus.CONFLICT.value()))
                .body(containsString("User with name " + userName1 + " already taken"));
    }

    @Test
    public void canGetUser() {
        User user1 = new User(userName1, firstName1, lastName1);
        User savedUser = userRepository.save(user1);
        Long userId = savedUser.getId();

        given()
                .when()
                .get("/api/user/" + userId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(USER_NAME_FIELD, Matchers.is(userName1))
                .body(FIRST_NAME_FIELD, Matchers.is(firstName1))
                .body(LAST_NAME_FIELD, Matchers.is(lastName1));
    }

    @Test
    public void shouldReturn404_whenGettingNonexistentUser() {
        int userId = 10000;
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/user/" + userId)
                .then()
                .statusCode(equalTo(HttpStatus.NOT_FOUND.value()))
                .body(containsString("User with id " + userId + " doesn't exist"));
    }

    @Test
    public void canGetAllUsers() {

        User user1 = new User(userName1, firstName1, lastName1);
        User user2 = new User(userName2, firstName2, lastName2);
        User user1Saved = userRepository.save(user1);
        User user2Saved = userRepository.save(user2);
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/user")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", hasItems(user1Saved.getId().intValue(), user2Saved.getId().intValue()))
                .body(FIRST_NAME_FIELD, hasItems(firstName1, firstName2))
                .body(LAST_NAME_FIELD, hasItems(lastName1, lastName2))
                .body(USER_NAME_FIELD, hasItems(userName1, userName2));

    }


    @Test
    public void canDeleteUserSuccessfully() {
        User user1 = new User(userName1, firstName1, lastName1);
        User savedUser = userRepository.save(user1);
        Long userId = savedUser.getId();
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/user/" + userId)
                .then()
                .statusCode(equalTo(HttpStatus.OK.value()))
                .body(containsString("User with id " + userId + " deleted successfully"));
    }


    @Test
    public void shouldReturn404_whenDeletingNonexistentUser() {
        int userId = 10000;
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/user/" + userId)
                .then()
                .statusCode(equalTo(HttpStatus.NOT_FOUND.value()))
                .body(containsString("User with id " + userId + " doesn't exist"));
    }


}




