package home.library.repository;

import home.library.entity.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource
public interface TicketRepository extends CrudRepository<Ticket, Integer>
{
    @Query("select t from Ticket t where t.book.id=:id")
    List<Ticket> findTicketByBook(@Param(value = "id")Integer id);
}
