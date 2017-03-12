package home.library.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * ФИО
 */
@Embeddable
public class PersonInfo
{
    @Column(name = "last_name",nullable = false)
    private String lastName;
    public String getLastName(){return this.lastName;}
    public void setLastName(String lastName){this.lastName = lastName;}

    @Column(name = "first_name",nullable = false)
    private String firstName;
    public String getFirstName(){return this.firstName;}
    public void setFirstName(String firstName){this.firstName = firstName;}

    @Column(name = "patronymic")
    private String patronymic;
    public String getPatronymic(){return this.patronymic;}
    public void setPatronymic(String patronymic){this.patronymic = patronymic;}

    @Temporal(TemporalType.DATE)
    private Date birth;
    public Date getBirth(){return this.birth;}
    public void setBirth(Date birth){this.birth = birth;}

    public PersonInfo(){}
    public PersonInfo(String lastName, String firstName, String patronymic, Date birth)
    {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.birth = birth;
    }
    public PersonInfo(String lastName, String firstName, Date birth)
    {
        this.lastName = lastName;
        this.firstName = firstName;
        this.birth = birth;
    }
    public PersonInfo(String lastName, String firstName, String patronymic)
    {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
    }

    public String getFullName()
    {
        String fullName = "";
        if(this.lastName!=null)
            fullName+=(lastName.isEmpty())?"":(this.lastName+" ");
        if(this.firstName!=null)
            fullName+=(firstName.isEmpty())?"":(this.firstName+" ");
        if(this.patronymic!=null)
            fullName+=(patronymic.isEmpty())?"":(this.patronymic+" ");
        return fullName;
    }
}
