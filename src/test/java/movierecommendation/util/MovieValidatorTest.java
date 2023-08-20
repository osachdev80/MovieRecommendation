package movierecommendation.util;

import movierecommendation.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovieValidatorTest {

    @InjectMocks
    private MovieValidator movieValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateMovieValid() {
        // Create a Movie with all required fields
        Movie validMovie = new Movie();
        validMovie.setTitle("Valid Title");
        validMovie.setGenre("Action");
        validMovie.setDirector("Director");

        // Validate the movie
        boolean isValid = movieValidator.validateMovie(validMovie);

        assertTrue(isValid);
    }

    @Test
    public void testValidateMovieMissingTitle() {
        // Create a Movie with a missing title
        Movie movie = new Movie();
        movie.setGenre("Action");
        movie.setDirector("Director");

        // Validate the movie
        boolean isValid = movieValidator.validateMovie(movie);

        assertFalse(isValid);
    }

    @Test
    public void testValidateMovieMissingGenre() {
        // Create a Movie with a missing genre
        Movie movie = new Movie();
        movie.setTitle("Valid Title");
        movie.setDirector("Director");

        // Validate the movie
        boolean isValid = movieValidator.validateMovie(movie);

        assertFalse(isValid);
    }

    @Test
    public void testValidateMovieMissingDirector() {
        // Create a Movie with a missing director
        Movie movie = new Movie();
        movie.setTitle("Valid Title");
        movie.setGenre("Action");

        // Validate the movie
        boolean isValid = movieValidator.validateMovie(movie);

        assertFalse(isValid);
    }

    @Test
    public void testValidateMovieAllMissing() {
        // Create a Movie with all missing required fields
        Movie movie = new Movie();

        // Validate the movie
        boolean isValid = movieValidator.validateMovie(movie);

        assertFalse(isValid);
    }
}
