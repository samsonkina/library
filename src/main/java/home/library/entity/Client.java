package home.library.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity(name = "Client")
@Table(name= "client")
public class Client
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    public Integer getId(){return this.id;}
    public void setId(Integer id) {this.id = id;}

    @Embedded
    private PersonInfo personInfo;
    public PersonInfo getPersonInfo(){return this.personInfo;}
    public void setPersonInfo(PersonInfo personInfo){this.personInfo = personInfo;}

    @Column(length = 20)
    private String phone;
    public String getPhone(){return this.phone;}
    public void setPhone(String phone){this.phone = phone;}

    @Column(nullable = false)
    private String address;
    public String getAddress(){return this.address;}
    public void setAddress(String address){this.address = address;}

    /**
     * список "билетов" читателя
     * один читатеь может иметь несколько билетов
     */
    @JsonManagedReference
    @OneToMany
    @JoinColumn(name="currentticket_id", nullable=true)
    private List<Ticket> currentTicket;
    public void setCurrentTicket(List<Ticket> currentTicket){this.currentTicket = currentTicket;}
    public List<Ticket> getCurrentTicket(){return this.currentTicket;}

    /**
     * история билетов читателя (когда и какие книги брал)
     */
  @JsonManagedReference
    @OneToMany
    @JoinColumn(name="history_id", nullable=true)
    private List<Ticket> history = new ArrayList<>();
    public void setHistory(List<Ticket> history){this.history = history;}
    public List<Ticket> getHistory(){return this.history;}

    public Client(){}
    public Client(String firstName, String lastName, String patronymic, Date birth, String phone, String address)
    {
        this.personInfo = new PersonInfo(firstName, lastName, patronymic, birth);
        this.phone = phone;
        this.address = address;
    }
}
