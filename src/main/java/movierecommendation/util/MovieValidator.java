package movierecommendation.util;

import movierecommendation.model.Movie;
import org.springframework.stereotype.Service;

@Service
public class MovieValidator {

    public boolean validateMovie(Movie movie) {
        return movie.getTitle() != null
                && movie.getGenre() != null
                && movie.getDirector() != null;
    }

}
