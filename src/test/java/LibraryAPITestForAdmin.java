import org.testng.Assert;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LibraryAPITestForAdmin {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "http://localhost:7081";
        RestAssured.authentication = RestAssured.preemptive().basic("admin", "password");
    }

    @Test
    public void createNewBook() {
        String requestBody = """
        {
            "id": 1,
            "title": "Jadunama",
            "author": "Javed Akhtar and Arvind Mandloi"
        }
        """;

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();
        String responseBody = response.asString();

        if (statusCode == 208) {
            Assert.assertEquals(statusCode, 208, "Expected status code 208");
        } else if (statusCode == 201) {
            Assert.assertEquals(statusCode, 201, "Expected status code 201");
        } else if (statusCode == 400) {
            Assert.assertEquals(statusCode, 400, "Expected status code 400");
            Assert.assertTrue(responseBody.toLowerCase().contains("invalid input parameters"), "Error message not found in the response");
        } else {
            Assert.fail("Failed to create a book with status code: " + statusCode);
        }

        System.out.println("Response : " + responseBody);
    }

    @Test
    public void updateBook() {
        String requestBody = """
    {
        "id": 1,
        "title": "Jadma",
        "author": "Javed Akhtar and Arvind Mandloi"
    }
    """;

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/api/books/10");

        int statusCode = response.getStatusCode();
        String responseBody = response.asString();

        System.out.println("Response : " + responseBody); // Print the response body

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
    public void deleteBook() {
        Response response = RestAssured.given()
                .when()
                .delete("/api/books/1");

        int statusCode = response.getStatusCode();
        String responseBody = response.asString();

        if (statusCode == 200) {
            Assert.assertEquals(statusCode, 200, "Expected status code 200");
        } else if (statusCode == 403) {
            Assert.assertEquals(statusCode, 403, "Expected status code 403");
        } else if (statusCode == 404) {
            Assert.assertEquals(statusCode, 404, "Expected status code 404");
        } else {
            Assert.fail("Failed to delete book with status code: " + statusCode);
        }

        System.out.println("Response : " + responseBody);
    }

    @Test
    public void getAllBooks() {
        Response response = RestAssured.get("/api/books");
        int statusCode = response.getStatusCode();
        String responseBody = response.asString();

        Assert.assertEquals(statusCode, 200, "Expected status code 200");
        System.out.println("Response : " + responseBody);
    }

    @Test
    public void getBookByID() {
        Response response = RestAssured.get("/api/books/5");
        int statusCode = response.getStatusCode();
        String responseBody = response.asString();

        if (statusCode == 200) {
            Assert.assertEquals(statusCode, 200, "Expected status code 200");
        } else if (statusCode == 404) {
            Assert.assertEquals(statusCode, 404, "Expected status code 404");
        } else {
            Assert.fail("Failed to get book by ID with status code: " + statusCode);
        }

        System.out.println("Response : " + responseBody);
    }
}
