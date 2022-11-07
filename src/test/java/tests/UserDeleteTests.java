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

@Epic("Deletion cases")
@Feature("Deletion")
public class UserDeleteTests extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();


    @Description("This tests checks user deletion")
    @DisplayName("User deletion: positive test")
    @Test
    public void deleteAuthorizedUser(){
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

        //DELETE
        Response responseDeleteUser =apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/"+userId,
                header,
                cookie);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);

        //CHECK DELETION
       responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);
       Assertions.assertResponseCodeEquals(responseGetAuth, 400);
       Assertions.assertResponseTextEquals(responseGetAuth, "Invalid username/password supplied");

    }


    @Description("This tests checks user deletion if other user is authorized")
    @DisplayName("User deletion: negative test: other user is authorized")
    @Test
    public void deleteOtherAuthorizedUser(){

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

        //DELETE
        Response responseDeleteUser =apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/"+userId,
                header2,
                cookie2);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);

        //CHECK DELETION

        Map <String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));



        responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);


    }
    @Description("This tests checks user deletion of user which can't be deleted")
    @DisplayName("User deletion: negative test: undeleteable user")
    @Test
    public void deleteUndeletableAuthorizedUser(){



        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);


        String cookie = super.getCookie(responseGetAuth, "auth_sid");
        String header = super.getHeader(responseGetAuth, "x-csrf-token");
        String userId ="2";
        //DELETE
        Response responseDeleteUser =apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/"+userId,
                header,
                cookie);

        responseDeleteUser.then().log().all();

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

        //CCHECK DELETION
        responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);


    }
}
