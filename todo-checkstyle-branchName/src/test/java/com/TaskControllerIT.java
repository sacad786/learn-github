package com;


import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.taskuser.TaskUserApplication;
import com.taskuser.dto.TaskUpdateDto;
import com.taskuser.model.Task;
import com.taskuser.model.User;
import com.taskuser.repository.TaskRepository;
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
public class TaskControllerIT {
    @Value("${local.server.port}")
    protected int serverPort;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    private String firstName1 = "smith";
    private String userName1 = "cliff";
    private String lastName1 = "john";

    private String NAME_FIELD = "name";
    private String DESCRIPTION_FIELD = "description";
    private String DATETIME_FIELD = "date_time";


    private String name1 = "coding";
    private String description1 = "spring todo";
    private String name2 = "reading";
    private String description2 = "read react book";


    @Before
    public void setUp() {

        RestAssured.port = serverPort;
        userRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    public void canCreateTaskSuccessfully() {
        User user = new User(userName1, firstName1, lastName1);
        userRepository.save(user);
        Long userId = user.getId();
        Task task = new Task(name1, description1, user);


        given()
                .body(task).contentType(ContentType.JSON)
                .when()
                .post("/api/user/" + userId + "/task")
                .then()
                .statusCode(equalTo(HttpStatus.OK.value()))
                .body(NAME_FIELD, is(name1))
                .body(DESCRIPTION_FIELD, is(description1))
                .body(DATETIME_FIELD, not(nullValue()));

    }

    @Test
    public void shouldReturn404_whenCreatingTaskForNonexistentUser() {
        int userId = 1000000;
        Task task = new Task(name1, description1, null);
        new Task();
        given()
                .body(task).contentType(ContentType.JSON)
                .when()
                .post("/api/user/" + userId + "/task")
                .then()
                .statusCode(equalTo(HttpStatus.NOT_FOUND.value()))
                .body(containsString("User with id " + userId + " doesn't exist"));
    }

    @Test
    public void canGetATaskSuccessfully() {
        User user = new User(userName1, firstName1, lastName1);
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();
        Task task = new Task(name1, description1, user);
        Task savedTask = taskRepository.save(task);
        Long taskId = savedTask.getId();
        given()
                .when()
                .get("/api/user/" + userId + "/task/" + taskId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(NAME_FIELD, Matchers.is(name1))
                .body(DESCRIPTION_FIELD, Matchers.is(description1))
                .body(DATETIME_FIELD, Matchers.is(not(nullValue())));

    }


    @Test
    public void shouldReturn404_whenGettingTaskForNonexistentTask() {
        User user = new User(userName1, firstName1, lastName1);
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();
        int taskId = 10000000;
        given()
                .when()
                .get("/api/user/" + userId + "/task/" + taskId)
                .then()
                .statusCode(equalTo(HttpStatus.NOT_FOUND.value()))
                .body(containsString("Task with id " + taskId + " doesn't exist"));
    }

    @Test
    public void canGetAllTasksSuccessfully() {
        User user = new User(userName1, firstName1, lastName1);
        userRepository.save(user);
        Long userId = user.getId();
        Task task1 = new Task(name1, description1, user);
        Task task2 = new Task(name2, description2, user);
        Task task1Saved = taskRepository.save(task1);
        Task task2Saved = taskRepository.save(task2);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/user/task/all")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", hasItems(task1Saved.getId().intValue(), task2Saved.getId().intValue()))
                .body(NAME_FIELD, hasItems(name1, name2))
                .body(DESCRIPTION_FIELD, hasItems(description1, description2))
                .body(DATETIME_FIELD, hasItems((not(nullValue()))));

    }


    @Test
    public void canGetAllTasksForAUserSuccessfully() {
        User user = new User(userName1, firstName1, lastName1);
        userRepository.save(user);
        Long userId = user.getId();
        Task task1 = new Task(name1, description1, user);
        Task task2 = new Task(name2, description2, user);
        Task task1Saved = taskRepository.save(task1);
        Task task2Saved = taskRepository.save(task2);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/user/" + userId + "/task")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", hasItems(task1Saved.getId().intValue(), task2Saved.getId().intValue()))
                .body(NAME_FIELD, hasItems(name1, name2))
                .body(DESCRIPTION_FIELD, hasItems(description1, description2))
                .body(DATETIME_FIELD, hasItems((not(nullValue()))));
    }

    @Test
    public void shouldReturn404_whenGettingAllTasksForANonexistentUser() {
        int userId = 10000;
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/user/" + userId + "/task")
                .then()
                .statusCode(equalTo(HttpStatus.NOT_FOUND.value()))
                .body(containsString("User with id " + userId + " doesn't exist"));
    }

    @Test
    public void shouldReturn404_whenGettingTaskForNonexistentUser() {
        int userId = 1000;
        Task task = new Task(name1, description1, null);
        Task taskSaved = taskRepository.save(task);
        Long taskId = taskSaved.getId();
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("api/user/" + userId + "/task/" + taskId)
                .then()
                .statusCode(equalTo(HttpStatus.NOT_FOUND.value()))
                .body(containsString("User with id " + userId + " doesn't exist"));
    }

    @Test
    public void shouldUpdateTaskInfoSuccessfully() {
        User user = new User(userName1, firstName1, lastName1);
        User userSaved = userRepository.save(user);
        Long userId = userSaved.getId();
        Task task = new Task(name1, description1, user);
        Task taskSaved = taskRepository.save(task);
        Long taskId = taskSaved.getId();
        TaskUpdateDto updateTaskInfo = new TaskUpdateDto(name2, description2, null);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(updateTaskInfo).contentType(ContentType.JSON)
                .when()
                .put("api/user/" + userId + "/task/" + taskId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(NAME_FIELD, Matchers.is(name2))
                .body(DESCRIPTION_FIELD, Matchers.is(description2));
    }

    @Test
    public void shouldReturn404_whenUpdatingNonexistentTask() {
        User user = new User(userName1, firstName1, lastName1);
        User userSaved = userRepository.save(user);
        Long userId = userSaved.getId();
        int taskId = 100000;
        TaskUpdateDto updateTaskInfo = new TaskUpdateDto(name2, description2, null);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(updateTaskInfo).contentType(ContentType.JSON)
                .when()
                .put("api/user/" + userId + "/task/" + taskId)
                .then()
                .statusCode(equalTo(HttpStatus.NOT_FOUND.value()))
                .body(containsString("Task with id " + taskId + " doesn't exist"));

    }

    @Test
    public void canDeleteTaskSuccessfully() {
        User user = new User(userName1, firstName1, lastName1);
        userRepository.save(user);
        Long userId = user.getId();
        Task task1 = new Task(name1, description1, user);
        Task task1Saved = taskRepository.save(task1);
        Long taskId = task1Saved.getId();
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("api/user/" + userId + "/task/" + taskId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("Task with id " + taskId + " deleted"));

    }

    @Test
    public void shouldReturn404_whenDeletingNonexistentTask() {
        User user = new User(userName1, firstName1, lastName1);
        userRepository.save(user);
        Long userId = user.getId();
        int taskId = 1000000;
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("api/user/" + userId + "/task/" + taskId)
                .then()
                .statusCode(equalTo(HttpStatus.NOT_FOUND.value()))
                .body(containsString("Task with id " + taskId + " doesn't exist"));


    }


}
