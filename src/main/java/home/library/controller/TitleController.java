package home.library.controller;

import home.library.entity.Author;
import home.library.entity.Genre;
import home.library.entity.Title;
import home.library.repository.AuthorRepository;
import home.library.repository.GenreRepository;
import home.library.repository.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Работа со списком произведений
 * методы аналогичны другим контроллерам
 */
@Controller
public class TitleController
{
    @Autowired
    TitleRepository titleRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    GenreRepository genreRepository;

    @RequestMapping(value = URIPath.PATH_TITLE, method = RequestMethod.GET)
    public String titlePage(Model model)
    {
        model.addAttribute("titles", titleRepository.findAll());
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        return "title";
    }

    @RequestMapping(value = URIPath.PATH_TITLE_ADD, method = RequestMethod.POST)
    public String addTitle(@RequestParam(value = "name") String name,
                           @RequestParam(value = "authorIds", required = false) List<Integer> authorIds,
                           @RequestParam(value = "genreIds", required = false) List<Integer> genreIds)
    {
        List<Author> authors = authorRepository.findAll(authorIds);
        List<Genre> genres = genreRepository.findAll(genreIds);
        Title title = titleRepository.save(new Title(name, genres, authors));
        if (title == null)
            return URIPath.REDIRECT + URIPath.PATH_TITLE;
        for (Author author : authors)
        {
            List<Title> titles = author.getTitles();
            if (!titles.contains(title))
                titles.add(title);
        }
        authorRepository.save(authors);
        return URIPath.REDIRECT + URIPath.PATH_TITLE;
    }

    @RequestMapping(value = URIPath.PATH_TITLE_EDIT + "/{id}", method = RequestMethod.GET)
    public String editTitlePage(@PathVariable(name = "id") Integer id, Model model)
    {
        Title title = titleRepository.findOne(id);
        if (title == null)
        {
            return URIPath.REDIRECT + URIPath.PATH_TITLE;
        }
        model.addAttribute("title", title);
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        return "title_edit";
    }

    @RequestMapping(value = URIPath.PATH_TITLE_EDIT, method = RequestMethod.POST)
    public String editTitle(@RequestParam(value = "id") Integer id,
                            @RequestParam(value = "name", required = true) String name,
                            @RequestParam(value = "authorIds", required = false) List<Integer> authorIds,
                            @RequestParam(value = "genreIds", required = false) List<Integer> genreIds)
    {

        List<Author> authors = authorRepository.findAll(authorIds);
        List<Genre> genres = genreRepository.findAll(genreIds);
        Title title = titleRepository.findOne(id);

        List<Author> authorsOld = title.getAuthors();
        title.setName(name);
        title.setAuthors(authors);
        title.setGenres(genres);
        title = titleRepository.save(title);


        if (title == null)
            return URIPath.REDIRECT + URIPath.PATH_TITLE;
        for (Author author : authorsOld)
        {
            List<Title> titles = author.getTitles();
            if (titles.contains(title))
                titles.remove(title);
            author.setTitles(titles);
        }
        authorRepository.save(authors);

        for (Author author : authors)
        {
            List<Title> titles = author.getTitles();
            if (!titles.contains(title))
                titles.add(title);
            author.setTitles(titles);
        }
        authorRepository.save(authors);

        return URIPath.REDIRECT + URIPath.PATH_TITLE;

    }

    @RequestMapping(value = URIPath.PATH_TITLE_DELETE, method = RequestMethod.POST)
            public String deleteTitle(@RequestParam(value = "id")Integer id)
    {
        Title title = titleRepository.findOne(id);

        List<Author> authors = title.getAuthors();
        for (Author author : authors)
        {
            List<Title> titles = author.getTitles();
            if (titles.contains(title))
                titles.remove(title);
            author.setTitles(titles);
        }
        authorRepository.save(authors);
        titleRepository.delete(id);

        return URIPath.REDIRECT+URIPath.PATH_TITLE;
    }
}
