package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import movierecommendation.MovieRecommendation;
import movierecommendation.model.Movie;
import movierecommendation.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = MovieRecommendation.class)
@AutoConfigureMockMvc
public class MovieControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MovieService movieService;

    @Test
    public void testGetAllMovies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/movies/getMovies"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testAddMovie() throws Exception {
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setGenre("Action");
        movie.setDirector("Test Director");

        when(movieService.addMovie(movie)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("Movie added successfully"));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/movies/addMovie")
                        .content(asJsonString(movie))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testRateMovie() throws Exception {
        Long movieId = 1L;
        double rating = 4.5;

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/movies/" + movieId + "/rate")
                        .param("rating", String.valueOf(rating))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testAddMovieValidationFailure() throws Exception {
        Movie movie = new Movie();
        when(movieService.addMovie(movie)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Movie Not added"));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/movies/addMovie")
                        .content(asJsonString(movie))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testRateMovieInvalidRating() throws Exception {
        Long movieId = 1L;
        double invalidRating = 6.0; // Invalid rating value

        when(movieService.rateMovie(movieId, invalidRating)).thenReturn(ResponseEntity.badRequest().body("Invalid rating"));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/movies/" + movieId + "/rate")
                        .param("rating", String.valueOf(invalidRating))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testRecommendMoviesByRatingsAndGenre() throws Exception {
        List<Movie> sampleMovies = new ArrayList<>();
        sampleMovies.add(Movie.builder()
                .genre("Action")
                .title("Movie 2")
                .releaseYear(2010)
                .numberOfRatings(1)
                .director("Director")
                .averageRating(2)
                .build());
        sampleMovies.add(Movie.builder()
                .genre("Action")
                .title("Movie 1")
                .releaseYear(2012)
                .numberOfRatings(1)
                .director("Director")
                .averageRating(5)
                .build());

        when(movieService.recommendMoviesByRatingsAndGenre(anyString())).thenReturn(sampleMovies);

        mockMvc.perform(MockMvcRequestBuilders.get("/movies/recommend").param("genre", "Action"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Movie 2")))
                .andExpect(jsonPath("$[1].title", is("Movie 1")));

        // Verify that the controller calls the service with the correct genre
        verify(movieService).recommendMoviesByRatingsAndGenre("Action");
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


