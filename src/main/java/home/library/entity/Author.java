package home.library.entity;

import javax.persistence.*;
import java.util.List;


@Entity(name = "Author")
@Table(name="author")
public class Author
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    public Integer getId(){return this.id;}
    public void setId(Integer id){this.id = id;}

    /**
     * ФИО
     */
    @Embedded
    private PersonInfo personInfo;
    public PersonInfo getPersonInfo(){return this.personInfo;}
    public void setPersonInfo(PersonInfo personInfo){this.personInfo = personInfo;}

    /**
     * Список произведений автора
     * Один автор может иметь несколько произведений
     */
    @ManyToMany(targetEntity = Title.class, fetch = FetchType.EAGER)
    private List<Title> titles;
    public List<Title> getTitles(){return this.titles;}
    public void setTitles(List<Title> titles){this.titles = titles;}

    public Author(){}
    public Author(PersonInfo personInfo, List<Title> titles)
    {
        this.personInfo = personInfo;
        this.titles = titles;
    }
    public Author(String lastName, String firstName, String patronymic, List<Title> titles)
    {
        this.personInfo=new PersonInfo(lastName,firstName,patronymic);
        this.titles=titles;
    }
    public Author(PersonInfo personInfo)
    {
        this.personInfo=personInfo;
    }
    public Author(String lastName, String firstName, String patronymic)
    {
        PersonInfo personInfo = new PersonInfo(lastName, firstName, patronymic);
        setPersonInfo(personInfo);
    }


}
