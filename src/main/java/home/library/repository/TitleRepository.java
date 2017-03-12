package home.library.repository;

import home.library.entity.Title;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource
public interface TitleRepository extends CrudRepository<Title, Integer>
{
    @Query("select t from Title t")
    List<Title> findAll();

    @Query("SELECT t FROM Title t WHERE t.id in :ids")
    List<Title> findAll(@Param("ids") List<Integer> titleIds);



}
