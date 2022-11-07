package tests;




import io.restassured.response.Response;

import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import lib.Assertions;


import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

import lib.ApiCoreRequests;





@Epic("Authorization cases")
@Feature("Authorization")
public class UserAuthTests extends BaseTestCase {
    String cookie;
    String header;
    int userIdOnAuth;

    private final ApiCoreRequests apiCoreRequests= new ApiCoreRequests();

    @BeforeEach
    public void loginUser(){

        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");


        Response responseGetAuth =
                apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login",authData);


        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");




    }

    @Test
    @Description("This test successfully authorize user by email and password")
    @DisplayName("Test positive auth user")
    public void testAuthUser(){

        Response responseCheckAuth = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/auth", this.header, this.cookie);

        responseCheckAuth.then().log().all();


        Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);


    }

    @Description("This tests checks authorization status without sending auth cookie or token")
    @DisplayName("Test negative auth user")
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "header"})
    public void testNegativeUser(String condition){




        String url = "https://playground.learnqa.ru/api/user/auth";

        if (condition.equals("cookie")){
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(url, this.cookie);
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);

        }
        else if (condition.equals("header")){
            Response responseForCheck=apiCoreRequests.makeGetRequestWithToken(url, this.header);
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        }
        else {
            throw new IllegalArgumentException("Condition value is not known: "+ condition);
        }







    }
}
