package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import movierecommendation.controller.MovieController;
import movierecommendation.model.Movie;
import movierecommendation.repository.MovieRepository;
import movierecommendation.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
@ComponentScan(basePackages = "movierecommendation.controller")
@ContextConfiguration(classes = MovieService.class)
public class MovieControllerTest {

    @MockBean
    private MovieRepository movieRepository;

    @MockBean
    private MovieService movieService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllMovies() throws Exception {
        Movie movie1 = buildMovie();
        Movie movie2 = new Movie();

        when(movieRepository.findAll()).thenReturn(Arrays.asList(movie1, movie2));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/getAllEmployees"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].firstName").value("John"))
//                .andExpect(jsonPath("$[0].surname").value("Smith"))
//                .andExpect(jsonPath("$[0].niNumber").value("WE123222A"));

        mockMvc.perform(MockMvcRequestBuilders.get("/movies/getMovies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testAddMovie_Success() throws Exception {
        Movie movie = buildMovie();

        when(movieRepository.existsByTitleAndReleaseYear(anyString(), anyInt())).thenReturn(false);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        mockMvc.perform(post("/movies/addMovie")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("Movie added successfully"));
    }

    @Test
    public void testAddMovie_MovieAlreadyExists() throws Exception {
        Movie movie = buildMovie();

        when(movieRepository.existsByTitleAndReleaseYear("Test Title", 2020)).thenReturn(true);

        mockMvc.perform(post("/movies/addMovie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(movie)))
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

    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
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
//
//    import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import randomactivity.client.ExternalApiClient;
//import randomactivity.model.RandomActivity;
//import randomactivity.service.RandomActivityService;
//
//import java.util.Optional;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//    @WebMvcTest(RandomActivity.class)
//    @ComponentScan(basePackages = "randomactivity.controller")
//    @ContextConfiguration(classes = RandomActivityService.class)
//
//    public class RandomActivityTest {
//        @Autowired
//        private MockMvc mockMvc;
//
//        @MockBean
//        private ExternalApiClient externalApiClient;
//
//        @Test
//        public void test_getActivityEndPoint_DTO() throws Exception {
//            RandomActivity randomActivity = RandomActivity.builder()
//                    .activity("My Activity")
//                    .accessibility("Accessibilty")
//                    .key("Key")
//                    .participants(1)
//                    .price(2.99)
//                    .type("Type")
//                    .build();
//            when(externalApiClient.getRandomActivity()).thenReturn(ResponseEntity.of(Optional.of(randomActivity)));
//            mockMvc.perform(MockMvcRequestBuilders.get("/randomActivities"))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.activity").value("My Activity"))
//                    .andExpect(jsonPath("$.accessibility").value("Accessibilty"))
//                    .andExpect(jsonPath("$.key").value("Key"));
//        }
//
//        @Test
//        public void test_getActivityEndPoint_JSON() throws Exception {
//            String jsonData = "{\"Activity\":\"Activty\"}";
//
//            when(externalApiClient.getRawJsonResponse()).thenReturn(jsonData);
//
//            mockMvc.perform(MockMvcRequestBuilders.get("/randomActivities"))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.activity").value("My Activity"))
//                    .andExpect(jsonPath("$.accessibility").value("Accessibilty"))
//                    .andExpect(jsonPath("$.key").value("Key"));
//        }
//    }


}
