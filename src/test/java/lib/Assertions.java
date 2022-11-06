package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public  class Assertions {

    public static void assertJsonByName(Response response, String name, int expectedValue){
        response.then().assertThat().body("$", hasKey(name));
        int value = response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertCookieByName(Response response, String name, String expectedValue){
        response.then().assertThat().cookie(name, expectedValue);
        //String value = response.getCookie(name);
       // assertEquals(expectedValue, value, "Cookie value is not equal to expected value");
    }

    public static void assertHeaderByName(Response response, String name, String expectedValue){
        response.then().assertThat().header(name, expectedValue);

        //String value = response.getHeader(name);
      //  assertEquals(expectedValue, value, "Header value is not equal to expected value");
    }


}
