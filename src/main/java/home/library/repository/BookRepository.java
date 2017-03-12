package home.library.repository;

import home.library.entity.Book;
import home.library.entity.Title;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource
public interface BookRepository extends CrudRepository<Book,Integer>
{
    @Query("select b from Book b")
    List<Book> findAll();

    @Query("select count(b) from Book b where b.title=:title and b.ticket is null")
    Integer countBooksAvailableByTitle(@Param("title")Title title);

    @Query("select b from Book b where b.serialNumber=:num")
    Book findBookBySerialNumber(@Param("num")String num);

    @Query("select count(b) from Book b where b.available=true and b.title.id=:id")
    Integer countAvailableByTitleId(@Param("id") Integer titleId);
}
