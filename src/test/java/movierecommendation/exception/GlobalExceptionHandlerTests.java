package movierecommendation.exception;

import movierecommendation.exception.GlobalExceptionHandler;
import movierecommendation.exception.InvalidMovieDataException;
import movierecommendation.exception.MovieAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTests {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleMovieAlreadyExistsException() {
        MovieAlreadyExistsException exception = new MovieAlreadyExistsException("Movie already exists");
        ResponseEntity<String> response = globalExceptionHandler.handleMovieAlreadyExistsException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Movie already exists", response.getBody());
    }

    @Test
    public void testHandleInvalidMovieDataException() {
        InvalidMovieDataException exception = new InvalidMovieDataException("Invalid movie data");
        ResponseEntity<String> response = globalExceptionHandler.handleInvalidMovieDataException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid movie data", response.getBody());
    }

    @Test
    public void testHandleGenericException() {
        Exception exception = new Exception("Unexpected error");
        ResponseEntity<String> response = globalExceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred.", response.getBody());
    }
}

