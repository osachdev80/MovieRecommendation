package controller;

import movierecommendation.controller.MovieController;
import movierecommendation.model.Movie;
import movierecommendation.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MovieControllerTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieController movieController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }

    @Test
    public void testGetAllMovies() throws Exception {
        Movie movie1 = buildMovie();
        Movie movie2 = new Movie();

        when(movieRepository.findAll()).thenReturn(Arrays.asList(movie1, movie2));

        mockMvc.perform(get("/movies/getMovies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testAddMovie_Success() throws Exception {
        Movie movie = buildMovie();

        when(movieRepository.existsByTitleAndReleaseYear(anyString(), anyInt())).thenReturn(false);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        mockMvc.perform(post("/movies/addMovie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content()
                .andExpect(status().isCreated())
                .andExpect(content().string("Movie added successfully"));
    }

    @Test
    public void testAddMovie_MovieAlreadyExists() throws Exception {
        Movie movie = buildMovie();

        when(movieRepository.existsByTitleAndReleaseYear(anyString(), anyInt())).thenReturn(true);

        mockMvc.perform(post("/movies/addMovie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(/* JSON representation of movie */))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Movie already exists"));
    }

    @Test
    public void testRateMovie_Success() throws Exception {
        Movie movie = buildMovie();

        when(movieRepository.findById(anyLong())).thenReturn(java.util.Optional.of(movie));

        mockMvc.perform(post("/movies/{id}/rate", 1L)
                        .param("rating", "4.5"))
                .andExpect(status().isOk())
                .andExpect(content().string("Movie rated successfully"));
    }

    @Test
    public void testRateMovie_InvalidRatingValue() throws Exception {
        mockMvc.perform(post("/movies/{id}/rate", 1L)
                        .param("rating", "6.0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid rating value. Rating must be between 1 and 5."));
    }

    @Test
    public void testRateMovie_MovieNotFound() throws Exception {
        when(movieRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        mockMvc.perform(post("/movies/{id}/rate", 1L)
                        .param("rating", "4.0"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Movie Not Found"));
    }

    private Movie buildMovie() {
        return Movie.builder()
                .title("Test Title")
                .genre("Comedy")
                .averageRating(1)
                .director("Test Director")
                .numberOfRatings(2)
                .releaseYear(2020)
                .build();
    }
}
