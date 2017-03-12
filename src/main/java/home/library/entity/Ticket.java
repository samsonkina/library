package home.library.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;


@Entity(name="Ticket")
@Table(name="ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    public Integer getId(){return this.id;}
    public void setId(Integer id){this.id = id;}

    /**
     * много билетов могут принадлежать одному клиенту
     */
    @JsonBackReference
    @ManyToOne
    private Client client;
    public void setClient(Client client){this.client = client;}
    public Client getClient(){return this.client;}

    /**
     * в билете может быть только одна книга
     */
    @JsonManagedReference
    @OneToOne(mappedBy = "ticket")
    @JoinColumn(name="ticket_id", nullable=true)
    private Book book;
    public Book getBook(){return this.book;}
    public void setBook(Book book){this.book = book;}

    /**
     * дата взятия книги
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateBegin = new Date();
    public Date getDateBegin(){return this.dateBegin;}
    public void setDateBegin(Date dateBegin){this.dateBegin = dateBegin;}

    /**
     * дата ФАКТИЧЕСКОЙ сдачи книги
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateClose;
    public Date getDateClose(){return this.dateClose;}
    public void setDateClose(Date dateClose){this.dateClose = dateClose;}

    /**
     * крайний срок сдачи книги
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;
    public Date getDateEnd(){return this.dateEnd;}
    public void setDateEnd(Date dateEnd){this.dateEnd = dateEnd;}

    /**
     * сдана ли книга?
     */
    @Column(nullable = false)
    private Boolean closed = false;
    public Boolean getClosed(){return this.closed;}
    public void setClosed(Boolean closed){this.closed = closed;}

    public Ticket(){}
    public Ticket( Book book, Date dateEnd)
    {
        this.book = book;
        this.dateEnd = dateEnd;
    }

}
