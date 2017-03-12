package home.library.entity;

import javax.persistence.*;


@Entity(name = "Genre")
@Table(name="genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    public Integer getId(){return this.id;}
    public void setId(Integer id){this.id = id;}

    @Column(name = "name", nullable = false,length = 30,unique = true)
    private String name;
    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    public Genre(){}
    public Genre(String name)
    {
        this.name = name;
    }


}
