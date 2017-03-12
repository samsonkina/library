package home.library.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity(name="Book")
@Table(name="book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    public Integer getId(){return this.id;}
    public void setId(Integer id){this.id = id;}

    /**
     * заголовок(произведение) книги
     * несколько книг могут иметь одинаковый заголовок
     */
    @JsonBackReference
    @ManyToOne
    private Title title;
    public Title getTitle(){return this.title;}
    public void setTitle(Title title){this.title = title;}

    /**
     * серийный номер книги
     */
    @Column(name = "serial_number", nullable = false,length = 30,unique = true)
    private String serialNumber;
    public String getSerialNumber(){return this.serialNumber;}
    public void setSerialNumber(String serialNumber){this.serialNumber = serialNumber;}

    /**
     * один "билет"(заявка) - одна книга
     */
    @JsonBackReference
    @OneToOne
    private Ticket ticket;
    public Ticket getTicket(){return this.ticket;}
    public void setTicket(Ticket ticket){this.ticket = ticket;}

    /**
     * история билетов книги
     */
    @JsonBackReference
    @OneToMany
    private List<Ticket> ticketsHistory;
    public List<Ticket> getTicketsHistory(){return this.ticketsHistory;}
    public void setTicketsHistory(List<Ticket> ticketsHistory){this.ticketsHistory = ticketsHistory;}

    /**
     * имеется ли книга в библиотеке
     */
    @Column
    private Boolean available;
    public void setAvailable(Boolean available){this.available = available;}
    public Boolean getAvailable(){return this.available;}

    public Book(){}
    public Book(Title title, String serialNumber)
    {
        this.title = title;
        this.serialNumber = serialNumber;
    }



}
