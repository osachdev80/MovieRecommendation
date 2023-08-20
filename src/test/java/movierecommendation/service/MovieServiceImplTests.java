package movierecommendation.service;

import movierecommendation.model.Movie;
import movierecommendation.repository.MovieRepository;
import movierecommendation.util.MovieValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MovieServiceImplTests {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieValidator movieValidator;

    @InjectMocks
    private MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddMovie_ValidMovie() {
        Movie movie = buildMovie("Movie 1", "Drama", 2021);
        when(movieValidator.validateMovie(movie)).thenReturn(true);
        when(movieRepository.existsByTitleAndReleaseYear(movie.getTitle(), movie.getReleaseYear())).thenReturn(false);
        when(movieRepository.save(movie)).thenReturn(movie);

        ResponseEntity<String> response = movieService.addMovie(movie);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo("Movie added successfully");
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    void testAddMovie_InvalidMovie() {
        Movie movie = buildMovie("Movie 1", "Drama", 2021);
        when(movieValidator.validateMovie(movie)).thenReturn(false);

        ResponseEntity<String> response = movieService.addMovie(movie);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Missing mandatory fields: title, genre, and director are required");
        verify(movieRepository, never()).save(any());
    }

    @Test
    void testAddMovie_MovieAlreadyExists() {
        Movie movie = buildMovie("Movie 1", "Drama", 2021);
        when(movieValidator.validateMovie(movie)).thenReturn(true);
        when(movieRepository.existsByTitleAndReleaseYear(movie.getTitle(), movie.getReleaseYear())).thenReturn(true);

        ResponseEntity<String> response = movieService.addMovie(movie);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Movie already exists");
        verify(movieRepository, never()).save(any());
    }

    @Test
    void testRateMovie_ValidRating() {
        // Arrange
        Long movieId = 1L;
        double rating = 4.5;
        Movie movie = buildMovie("Movie 1", "Drama", 2021);
        movie.setId(movieId);
        movie.setAverageRating(3.0);
        movie.setNumberOfRatings(2);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        ResponseEntity<String> response = movieService.rateMovie(movieId, rating);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Movie rated successfully");
        assertThat(movie.getAverageRating()).isEqualTo(3.5);
        assertThat(movie.getNumberOfRatings()).isEqualTo(3);
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    void testRateMovie_InvalidRating() {
        Long movieId = 1L;
        double rating = 6.0;

        ResponseEntity<String> response = movieService.rateMovie(movieId, rating);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid rating value. Rating must be between 1 and 5.");
        verify(movieRepository, never()).save(any());
    }

    @Test
    void testRateMovie_MovieNotFound() {
        Long movieId = 1L;
        double rating = 4.0;
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = movieService.rateMovie(movieId, rating);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Movie Not Found");
        verify(movieRepository, never()).save(any());
    }

    @Test
    void testGetAllMovies() {
        List<Movie> expectedMovies = new ArrayList<>();
        expectedMovies.add(buildMovie("Movie 1", "Drama", 2021));
        expectedMovies.add(buildMovie("Movie 2", "Drama", 2022));
        when(movieRepository.findAll()).thenReturn(expectedMovies);

        List<Movie> actualMovies = movieService.getAllMovies();

        assertThat(actualMovies).isEqualTo(expectedMovies);
    }

    private Movie buildMovie(String title, String genre, int releaseYear) {
        return  Movie.builder()
                .title(title)
                .releaseYear(releaseYear)
                .averageRating(1)
                .director("Test Director")
                .genre(genre)
                .numberOfRatings(2)
                .build();
    }
}

