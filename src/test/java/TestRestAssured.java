import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;



import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static  org.hamcrest.Matchers.hasKey;

public class TestRestAssured {
    @Test
    public void testJsonParse() {
        JsonPath response = RestAssured

                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();


        String secondMessage = response.getString("messages.message[1]");
        System.out.println(secondMessage);


    }

    @Test
    public void testRedirectLocation() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();


        String locationHeader = response.getHeader("Location");
        System.out.println(locationHeader);


    }




    @Test
    public void testLongRedirect() {
        int responceCode = 0;
        String locationAdress = "https://playground.learnqa.ru/api/long_redirect";
        int iterationNumber = 0;

        while (responceCode != 200&&iterationNumber<20) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(locationAdress)
                    .andReturn();


            String locationHeader = response.getHeader("Location");
            System.out.println(locationHeader);
            locationAdress = locationHeader;

            responceCode = response.getStatusCode();
            System.out.println(responceCode);
            iterationNumber++;
        }


    }

    @Test
    public void testToken() throws InterruptedException {

        String url = "https://playground.learnqa.ru/ajax/api/longtime_job";

        JsonPath responseFirst = RestAssured
                .given()
                .get(url)
                .jsonPath();


        responseFirst.prettyPrint();
        String token =responseFirst.getString("token");
        System.out.println(token);

        Integer time =responseFirst.getInt("seconds");
        System.out.println(time);

        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        Response responseTimeNotPassed= RestAssured
                .given()
                .params(params)
                .get(url)
                .andReturn();
        responseTimeNotPassed.prettyPrint();
        String status = responseTimeNotPassed.jsonPath().getString("status");
        assertEquals("Job is NOT ready", status,
                "The status is not correct when time is not passed.");




        TimeUnit.SECONDS.sleep(time);
        Response responseTimePassed= RestAssured
                .given()
                .params(params)
                .get(url)
                .andReturn();
        responseTimePassed.prettyPrint();

        status = responseTimePassed.jsonPath().getString("status");


        assertEquals("Job is ready", status,
                "The status is not correct when time is not passed.");
        responseTimePassed.then().assertThat().body("$", hasKey("result"));










    }




}
