package movierecommendation.repository;

import movierecommendation.model.Movie;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByTitleAndReleaseYear(String title, int releaseYear);

    List<Movie> findByGenreIgnoreCase(String genre, Sort sort);
}