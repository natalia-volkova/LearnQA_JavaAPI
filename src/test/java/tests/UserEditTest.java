package tests;


import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
@Epic("Modification cases")
@Feature("Modification")
public class UserEditTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Description("This tests checks user modification")
    @DisplayName("User modification: positive test")
    @Test
    public void testEditJustCreatedTest(){



        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData);

        String userId= responseCreateAuth.jsonPath().getString("id");


        //LOGIN
        Map <String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);


        String cookie = super.getCookie(responseGetAuth, "auth_sid");
        String header = super.getHeader(responseGetAuth, "x-csrf-token");

        //EDIT
        String changedName = "Changed name";
        Map <String, String> editData = new HashMap<>();
        editData.put("firstName", changedName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/"+userId,
                        header,
                        cookie,
                        editData);


        Assertions.assertResponseCodeEquals(responseEditUser, 200);

        //GET

        Response responseUserData= apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        header,
                        cookie);


        Assertions.assertJsonByName(responseUserData, "firstName", changedName);

    }
    @Description("This tests checks user modification if user is not authorized")
    @DisplayName("User modification: negative test: User not authorized")
    @Test
    public void testEditUnauthorizedUser(){



        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData);

        String userId= responseCreateAuth.jsonPath().getString("id");

        //LOGIN
        Map <String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);


        String cookie = super.getCookie(responseGetAuth, "auth_sid");
        String header = super.getHeader(responseGetAuth, "x-csrf-token");


        //EDIT WITH EMPTY COOKIE AND TOKEN
        String changedName = "Changed name";
        Map <String, String> editData = new HashMap<>();
        editData.put("firstName", changedName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/"+userId,
                        "",
                        "",
                        editData);


        Assertions.assertResponseCodeEquals(responseEditUser, 400);

        //GET

        Response responseUserData= apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        header,
                        cookie);

        Assertions.assertJsonByName(responseUserData, "firstName", userData.get("firstName"));



    }
    @Description("This tests checks user modification if other user is authorized")
    @DisplayName("User modification: negative test: Other user is authorized")
    @Test
    public void testEditOtherUser(){



        //GENERATE USER1
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData);

        String userId= responseCreateAuth.jsonPath().getString("id");

        //GENERATE USER2
        Map<String, String> userData2 = DataGenerator.getRegistrationData();

        Response responseCreateAuth2 = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData2);




        //LOGIN USER2
        Map <String, String> authData2 = new HashMap<>();
        authData2.put("email", userData2.get("email"));
        authData2.put("password", userData2.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData2);


        String cookie2 = super.getCookie(responseGetAuth, "auth_sid");
        String header2 = super.getHeader(responseGetAuth, "x-csrf-token");


        //EDIT SECOND USER AUTHORIZED
        String changedName = "Changed name";
        Map <String, String> editData = new HashMap<>();
        editData.put("firstName", changedName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/"+userId,
                        header2,
                        cookie2,
                        editData);


        Assertions.assertResponseCodeEquals(responseEditUser, 400);

        //LOGIN
        Map <String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);


        String cookie = super.getCookie(responseGetAuth, "auth_sid");
        String header = super.getHeader(responseGetAuth, "x-csrf-token");

        //GET

        Response responseUserData= apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        header,
                        cookie);

        Assertions.assertJsonByName(responseUserData, "firstName", userData.get("firstName"));



    }
    @Description("This tests checks user modification if user email is not correct")
    @DisplayName("User modification: negative test: Incorrect email")
    @Test
    public void testEditIncorrectNewEmail(){



        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData);

        String userId= responseCreateAuth.jsonPath().getString("id");


        //LOGIN
        Map <String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);


        String cookie = super.getCookie(responseGetAuth, "auth_sid");
        String header = super.getHeader(responseGetAuth, "x-csrf-token");

        //EDIT
        String changedEmail = "emailtest.com";
        Map <String, String> editData = new HashMap<>();
        editData.put("email", changedEmail);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/"+userId,
                        header,
                        cookie,
                        editData);


        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser,"Invalid email format");

        //GET

        Response responseUserData= apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        header,
                        cookie);


        Assertions.assertJsonByName(responseUserData, "email", userData.get("email"));

    }
    @Description("This tests checks user modification if username is not correct")
    @DisplayName("User modification: negative test: Incorrect username")
    @Test
    public void testEditIncorrectUserName(){



        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData);

        String userId= responseCreateAuth.jsonPath().getString("id");


        //LOGIN
        Map <String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);


        String cookie = super.getCookie(responseGetAuth, "auth_sid");
        String header = super.getHeader(responseGetAuth, "x-csrf-token");

        //EDIT
        String changedName = "t";
        Map <String, String> editData = new HashMap<>();
        editData.put("username", changedName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/"+userId,
                        header,
                        cookie,
                        editData);

        responseEditUser.then().log().all();
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser,"{\"error\":\"Too short value for field username\"}");

        //GET

        Response responseUserData= apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/"+userId,
                        header,
                        cookie);


        Assertions.assertJsonByName(responseUserData, "username", userData.get("username"));

    }
}
