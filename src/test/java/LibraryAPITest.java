import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.fail;
import static org.hamcrest.Matchers.*;

public class LibraryAPITest {

    private static Properties properties;

    @BeforeClass
    public void setUp() {
        // Load configuration from properties file
        properties = new Properties();
        try {
            properties.load(new FileInputStream("config/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        RestAssured.baseURI = properties.getProperty("baseURI");
        RestAssured.authentication = RestAssured.preemptive().basic(
                properties.getProperty("userUsername"),
                properties.getProperty("Password")
        );
    }

    //1
    @Test
    public void testUnauthorizedCreateBook() {
        String requestBody = """
                {
                   "id": 11,
                   "title": "Great Gatsby",
                   "author": "Elroy O'Cridigan"
                 }
                """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();
        assertStatusCodeAndPrintResponse(StatusCodes.STATUS_CODE_UNAUTHORIZED, "You are not authorized to create the book - Expected status code 401 (Unauthorized)", response);
    }

    //2
    @Test
    void createNewBook() {
        String requestBody = """
                {
                  "id": 82,
                  "title": "To Kill a Mockingbird",
                  "author": "Osborn Hellmore"
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();

        if (statusCode == StatusCodes.STATUS_CODE_CREATED) {
            assertStatusCodeAndPrintResponse(StatusCodes.STATUS_CODE_CREATED, "Successfully created the book - Expected status code 201", response);
        } else {
            fail("Failed to create a book : " + statusCode);
        }
    }

    //3
    @Test
    void createNewBookWithEmptyTitle() {
        String requestBody = """
                {
                            "id": 100,
                            "title": "",
                            "author": "Ignaz Belch"
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

        if (statusCode == StatusCodes.STATUS_CODE_BAD_REQUEST) {
            assertStatusCodeAndPrintResponse(StatusCodes.STATUS_CODE_BAD_REQUEST, " Invalid | Empty Input Parameters in the Request - Expected status code 400", response);
        } else {
            fail("Actual Status code : " + statusCode);
        }
    }


    //4
    @Test
    void createDuplicateBook() {
        String requestBody = """
                 {
                    "id": 27,
                    "title": "Harry Potter and the Sorcerer's Stone",
                    "author": "Tressa Soane"
                  }
                """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();

        if (statusCode == StatusCodes.STATUS_CODE_ALREADY_EXISTS) {
            assertStatusCodeAndPrintResponse(StatusCodes.STATUS_CODE_ALREADY_EXISTS, "Book Already exists - Expected status code 208", response);
        } else {
            fail("Failed to create a duplicate book: " + statusCode);
        }
    }

    //5
    @Test
    void createBookWithMissingAuthor() {
        String requestBody = """
                {
                  "id": 87,
                  "title": "The Odyssey",
                  "author": ""
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();

        System.out.println(statusCode);

        if (statusCode == StatusCodes.STATUS_CODE_BAD_REQUEST) {
            assertStatusCodeAndPrintResponse(StatusCodes.STATUS_CODE_BAD_REQUEST, " Invalid | Empty Input Parameters in the Request - Expected status code 400", response);
        } else {
            fail("Actual Status code: " + statusCode);
        }
    }

    //6
    @Test
    void createBookWithMissingId() {
        String requestBody = """
                {
                   "id": ,
                   "title": "The Chronicles of Narnia",
                   "author": "Esra Jefferies"
                 }
                """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();

        if (statusCode == StatusCodes.STATUS_CODE_CREATED) {
            assertStatusCodeAndPrintResponse(StatusCodes.STATUS_CODE_CREATED, "Expected status code 201", response);
        } else {
            fail("Actual Status code: " + statusCode);
        }
    }


    //7
    @Test
    void createBookWithMissingTitleAndAuthor() {
        String requestBody = """
                {
                    "id": 63,
                    "title": "",
                    "author": ""
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();

        if (statusCode == StatusCodes.STATUS_CODE_BAD_REQUEST) {
            assertStatusCodeAndPrintResponse(StatusCodes.STATUS_CODE_BAD_REQUEST, "Invalid | Empty Input Parameters in the Request - Expected status code 400", response);
        } else {
            fail("Actual Status code: " + statusCode);
        }
    }

    //8
    @Test
    void createBookWithSpecialCharactersInTitle() {
        String requestBody = """
                {
                    "id": 83,
                    "title": "The @#$% 1984",
                    "author": "Lizzie Eadie"
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();

        if (statusCode == StatusCodes.STATUS_CODE_CREATED) {
            assertStatusCodeAndPrintResponse(StatusCodes.STATUS_CODE_CREATED, "Successfully created a book with special characters in the title - Expected status code 201", response);
        } else {
            fail("Actual Status code: " + statusCode);
        }
    }




    private void assertStatusCodeAndPrintResponse(int expectedStatusCode, String assertionMessage, Response response) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, assertionMessage);
        System.out.println("Response : " + response.asString());
        System.out.println(assertionMessage);
    }
}
