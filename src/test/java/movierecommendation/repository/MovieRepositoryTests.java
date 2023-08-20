package movierecommendation.repository;

import movierecommendation.model.Movie;
import movierecommendation.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MovieRepositoryTests {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testExistsByTitleAndReleaseYear() {
        Movie movie =  Movie.builder()
                .title("Movie 1")
                .releaseYear(2020)
                .averageRating(1)
                .director("Test Director")
                .numberOfRatings(2)
                .build();
        entityManager.persistAndFlush(movie);

        boolean exists = movieRepository.existsByTitleAndReleaseYear("Movie 1", 2020);

        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsByTitleAndReleaseYear_WhenMovieDoesNotExist() {
        boolean exists = movieRepository.existsByTitleAndReleaseYear("NonexistentMovie", 2022);

        assertThat(exists).isFalse();
    }

    @Test
    public void testFindByGenreIgnoreCase() {
        Movie movie1 =  Movie.builder()
                .title("Movie 1")
                .releaseYear(2020)
                .averageRating(1)
                .director("Test Director")
                .genre("action")
                .numberOfRatings(2)
                .build();

        Movie movie2 =  Movie.builder()
                .title("Movie 2")
                .releaseYear(2020)
                .averageRating(1)
                .director("Test Director 2")
                .numberOfRatings(2)
                .build();
        entityManager.persistAndFlush(movie1);
        entityManager.persistAndFlush(movie2);

        List<Movie> movies = movieRepository.findByGenreIgnoreCase("action", null);

        assertThat(movies).hasSize(1);
        assertThat(movies.get(0).getTitle()).isEqualTo("Movie 1");
    }

    @Test
    public void testFindByGenreIgnoreCase_WhenGenreNotFound() {
        // When
        List<Movie> movies = movieRepository.findByGenreIgnoreCase("Comedy", null);

        // Then
        assertThat(movies).isEmpty();
    }
}
