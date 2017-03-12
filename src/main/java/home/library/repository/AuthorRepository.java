package home.library.repository;

import home.library.entity.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource
public interface AuthorRepository extends CrudRepository<Author, Integer>
{
    @Query("select a from Author a")
    List<Author> findAll();

    @Query("select a from Author a where a.id in :ids")
    List<Author> findAll(@Param("ids") List<Integer> authorIds);

}
