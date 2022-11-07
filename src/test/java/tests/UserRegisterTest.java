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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
@Epic("Registration cases")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    private String url = "https://playground.learnqa.ru/api/user/";

    @Description("This test to check error if create user with existing email")
    @DisplayName("Test negative create user: existing email")
    @Test
    public void testCreateUserWithExistingEmail() {

        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();

        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);


        Response responseCreateUser = apiCoreRequests.makePostRequest(url, userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "Users with email '" + email + "' already exists");

    }

    @Description("This test successful user creation")
    @DisplayName("Test positive create user")
    @Test
    public void testCreateUserSuccessfully() {


        Map<String, String> userData;


        userData = DataGenerator.getRegistrationData();

        Response responseCreateUser = apiCoreRequests.makePostRequest(url, userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 200);

        Assertions.assertJsonHasField(responseCreateUser, "id");

    }

    @Description("This test to check error if create user with incorrect email")
    @DisplayName("Test negative create user: incorrect email")
    @Test
    public void testCreateUserWithIncorrectEmail() {

        String email = "vinkotovexample.com";

        Map<String, String> userData = new HashMap<>();

        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateUser = apiCoreRequests.makePostRequest(url, userData);
        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "Invalid email format");
    }

    @Description("This test to check error if create user with short username")
    @DisplayName("Test negative create user: short username")
    @Test
    public void testCreateUserWithVeryShortName() {

        String username = "1";

        Map<String, String> userData = new HashMap<>();

        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateUser = apiCoreRequests.makePostRequest(url, userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "The value of 'username' field is too short");
    }

    @Description("This test to check error if create user with long username")
    @DisplayName("Test negative create user: long username")
    @Test
    public void testCreateUserWithVeryLongName() {

        String username = "testdsimbols1testdsimbols2testdsimbols3testdsimbols4testdsimbols5testdsimbols6testdsimbols7testdsimbols1testdsimbols2testdsimbols3testdsimbols4testdsimbols5testdsimbols6testdsimbols7testdsimbols1testdsimbols2testdsimbols3testdsimbols4testdsimbols5testdsimbols6testdsimbols7";

        Map<String, String> userData = new HashMap<>();

        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateUser = apiCoreRequests.makePostRequest(url, userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "The value of 'username' field is too long");
    }

    @Description("This tests checks user creation without any parameter")
    @DisplayName("Test negative create user: parameter missed")
    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "firstName", "lastName", "username"})
    public void testCreateUserithoutRequiredField(String condition) {
        Map<String, String> userData;
        userData = DataGenerator.getRegistrationData();
        Response responseCreateUser;

        if (condition.equals("email")) {

            userData.remove("email");
            responseCreateUser = apiCoreRequests.makePostRequest(url, userData);



        } else if (condition.equals("password")) {
            userData.remove("password");
            responseCreateUser = apiCoreRequests.makePostRequest(url, userData);

        } else if (condition.equals("firstName")) {
            userData.remove("firstName");
            responseCreateUser = apiCoreRequests.makePostRequest(url, userData);

        } else if (condition.equals("lastName")) {
            userData.remove("lastName");
            responseCreateUser = apiCoreRequests.makePostRequest(url, userData);

        } else if (condition.equals("username")) {
            userData.remove("username");
            responseCreateUser = apiCoreRequests.makePostRequest(url, userData);
        } else {
            throw new IllegalArgumentException("Condition value is not known: " + condition);
        }

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "The following required params are missed: "+condition);


    }
}
