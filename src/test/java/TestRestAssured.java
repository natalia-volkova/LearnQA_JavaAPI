import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class TestRestAssured {
    @Test
    public void testJsonParse()
    {
        JsonPath response= RestAssured

                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();


        
        String secondMessage = response.getString("messages.message[1]");
        System.out.println(secondMessage);


    }

    @Test
    public void testRedirectLocation()
    {
        Response response= RestAssured
                .given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();


        String locationHeader=response.getHeader("Location");
        System.out.println(locationHeader);




    }
}
