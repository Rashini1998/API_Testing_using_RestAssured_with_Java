import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.fail;

public class LibraryAPITestForUser {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "http://localhost:7081";
        RestAssured.authentication = RestAssured.preemptive().basic("user", "password");
    }

    @Test
    void createNewBook() {
        String requestBody = """
        {
            "id": 6,
            "title": "Jadunama",
            "author": "Javed Akhtar and Arvind Mandloi"
        }
        """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();

        if (statusCode == 208) {
            assertStatusCodeAndPrintResponse(208, "Expected status code 208", response);
        } else if (statusCode == 201) {
            assertStatusCodeAndPrintResponse(201, "Expected status code 201", response);
        } else {
            fail("Failed to create a book : " + statusCode);
        }
    }

    @Test
    void getAllBooks() {
        Response response = get("/api/books");
        int statusCode = response.getStatusCode();

        if (statusCode == 200) {
            assertStatusCodeAndPrintResponse(200, "Expected status code 200", response);
        } else {
            fail("Fail to get all books : " + statusCode);
        }
    }

    @Test
    void getBookByID() {
        Response response = get("/api/books/5"); // replace 5 with the actual bookId
        int statusCode = response.getStatusCode();

        if (statusCode == 200) {
            assertStatusCodeAndPrintResponse(200, "Expected status code 200", response);
        } else if (statusCode == 403) {
            assertStatusCodeAndPrintResponse(403, "Expected status code 403", response);
        } else if (statusCode == 404) {
            assertStatusCodeAndPrintResponse(404, "Expected status code 404", response);
        } else {
            fail("Fail to get book by ID : " + statusCode);
        }
    }

    @Test
    void updateBook() {
        String requestBody = """
        {
            "id": 6,
            "title": "Jadunama",
            "author": "Javed Akhtar and Arvind Mandloi"
        }
        """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/api/books/2"); // replace 2 with the actual bookId

        int statusCode = response.getStatusCode();
        String responseBody = response.asString();

        if (statusCode == 200) {
            Assert.assertEquals(statusCode, 200, "Expected status code 200");
        } else if (statusCode == 208) {
            Assert.assertEquals(statusCode, 208, "Expected status code 208");
        } else if (statusCode == 400) {
            Assert.assertEquals(statusCode, 400, "Expected status code 400");
            Assert.assertTrue(responseBody.toLowerCase().contains("Book id is not matched".toLowerCase()), "Error message not found in the response");
        } else if (statusCode == 403) {
            Assert.assertEquals(statusCode, 403, "Expected status code 403");
        } else if (statusCode == 404) {
            Assert.assertEquals(statusCode, 404, "Expected status code 404");
        } else {
            Assert.fail("Failed to update book with status code: " + statusCode);
        }
    }

    @Test
    void deleteBook() {
        Response response = given()
                .when()
                .delete("/api/books/2"); // replace 2 with the actual bookId

        int statusCode = response.getStatusCode();

        if (statusCode == 200) {
            assertStatusCodeAndPrintResponse(200, "Expected status code 200", response);
        } else if (statusCode == 403) {
            assertStatusCodeAndPrintResponse(403, "Expected status code 403", response);
        } else if (statusCode == 404) {
            assertStatusCodeAndPrintResponse(404, "Expected status code 404", response);
        } else {
            fail("Failed to delete book : " + statusCode);
        }
    }

    private void assertStatusCodeAndPrintResponse(int expectedStatusCode, String assertionMessage, Response response) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, assertionMessage);
        System.out.println("Response : " + response.asString());
    }

    private void assertStatusCodeAndPrintResponseWithMessage(int expectedStatusCode, String assertionMessage, Response response, String message) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, assertionMessage);
        Assert.assertTrue(response.asString().toLowerCase().contains(message.toLowerCase()), "Error message not found in the response");
        System.out.println("Response : " + response.asString());
    }
}
