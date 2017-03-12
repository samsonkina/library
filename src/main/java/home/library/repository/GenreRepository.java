package home.library.repository;

import home.library.entity.Genre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource
public interface GenreRepository extends CrudRepository<Genre,Integer>
{
    @Query("select g from Genre g")
    List<Genre> findAll();

    @Query("select g from Genre g where g.id in :ids")
    List<Genre> findAll(@Param("ids") List<Integer> genreIds);
}
