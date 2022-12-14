package tests;


import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
@Epic("Get data cases")
@Feature("Get userdata cases")
public class UserGetTest extends BaseTestCase {

    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Description("This test checks get user data if user is not authorized")
    @DisplayName("Get user data: negative test: Not authorized user")
    @Test
    public void getUserDateNotAuth(){


        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/2");



        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }
    @Description("This test checks get user data ")
    @DisplayName("Get user data: positive test")
    @Test
    public void testGetUserDetailsAuthAsSameUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);


        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/2",
                        header,
                        cookie);


        String[] expectedFields = {"username", "firstName", "lastName", "email"};

        Assertions.assertJsonHasFields(responseCheckAuth, expectedFields);
    }
    @Description("This test checks get user data if other user is authorized")
    @DisplayName("Get user data: negative test: Other authorized user")
    @Test
    public void testGetUserDetailsAuthOtherUser(){

        //CREATE OTHER USER
        Map<String, String> userData;
        userData = DataGenerator.getRegistrationData();
        Response responseCreateUser = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateUser, 200);
        Assertions.assertJsonHasField(responseCreateUser, "id");

        //LOGIN OTHER USER

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);


        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");


        //Check details for not logged in user
        Response responseUserData = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/2",
                        header,
                        cookie);


        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }
}
