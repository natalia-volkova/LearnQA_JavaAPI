import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class TestRestAssured {
    @Test
    public void testJsonParse()
    {
        JsonPath response= RestAssured

                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();


        response.prettyPrint();
        String secondMessage = response.getString("messages.message[1]");

        System.out.println(secondMessage);




    }
}
