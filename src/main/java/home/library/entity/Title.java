package home.library.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Tensky on 01.03.2017.
 */
@Entity(name = "Title")
@Table(name="title")
public class Title
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    public Integer getId() {return this.id;}
    public void setId(Integer id) {this.id = id;}

    @Column(nullable = false)
    private String name;
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    @ManyToMany(targetEntity = Genre.class, fetch = FetchType.LAZY)
    private List<Genre> genres;
    public List<Genre> getGenres() {return this.genres;}
    public void setGenres(List<Genre> genres) {this.genres = genres;}

    @ManyToMany(targetEntity = Author.class,fetch = FetchType.EAGER,mappedBy = "titles")
    private List<Author> authors;
    public List<Author> getAuthors() {return this.authors;}
    public void setAuthors(List<Author> authors) {this.authors = authors;}

    public Title() {}
    public Title(String name, List<Genre> genres, List<Author> authors)
    {
        this.name = name;
        this.genres = genres;
        this.authors = authors;
    }

    public String authorsList()
    {
        String tmpFulNames = "";
        for (Author author : this.authors)
        {
            tmpFulNames+=author.getPersonInfo().getFullName() + "\n";
        }
        return tmpFulNames;

    }
}
