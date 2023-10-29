import io.restassured.RestAssured;
import io.restassured.response.Response;
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
    void createNewBook(){
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
        int statusCode =response.getStatusCode();
        if (statusCode == 208) {
            System.out.println("Book Already Exists : " + response.getStatusCode());
        }else if(statusCode == 201){
            System.out.println("Successfully created the book : " + statusCode);
        }else {
            fail("Failed to create a book : " + statusCode);
        }
        System.out.println("Response : " + response.asString());
    }

    @Test
    void getAllBooks(){
        //Response will be stored in "response"
        Response response = get("/api/books");
        int statusCode = response.getStatusCode();
        if (statusCode == 200) {
            System.out.println("Successfully Get All Books : " + response.getStatusCode());
        } else {
            fail("Fail to get all books : " + statusCode);
        }
        System.out.println("Response : " + response.asString());
    }

    @Test
    void getBookByID() {
        Response response = get("/api/books/5"); // Replace 5 with the actual book id you want to retrieve
        int statusCode = response.getStatusCode();

        if (statusCode == 200) {
            System.out.println("Successfully Get Book by ID : " + response.getStatusCode());
        } else if (statusCode == 403) {
            System.out.println("User does not have permission to get the book by ID: " + statusCode);
        } else if (statusCode == 404) {
            System.out.println("Book is not found : " + statusCode);
        } else {
            fail("Fail to get book by ID : " + statusCode);
        }
        System.out.println("Response : " + response.asString());
    }

    @Test
    void updateBook(){
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

        if (statusCode == 200) {
            System.out.println("Successfully update the book : " + statusCode);
        }else if(statusCode == 208){
            System.out.println("Book already exists : " + statusCode);
        }else if (statusCode == 400) {
            System.out.println("Invalid | Empty Input Parameters in the Request : " + response.getStatusCode());
        } else if (statusCode == 403) {
            System.out.println("User does not have permission to update the book : " + statusCode);
        } else if(statusCode == 404){
            System.out.println("Book is not found : " + statusCode);
        }else{
            fail("Failed to update book: " + statusCode);
        }
        System.out.println("Response : " + response.asString());
    }

    @Test
    void deleteBook() {
        Response response = given()
                .when()
                .delete("/api/books/2"); // Replace 1 with the actual bookId you want to delete

        int statusCode = response.getStatusCode();

        if (statusCode == 200) {
            System.out.println("Successfully delete the book : " + statusCode);
        } else if (statusCode == 403) {
            System.out.println("Forbidden - User does not have permission to delete the book : " + statusCode);
        } else if (statusCode == 404) {
            System.out.println("Book is not found : " + statusCode);
        } else {
            fail("Failed to delete book : " + statusCode);
        }
        System.out.println("Response : " + response.asString());
    }
}
