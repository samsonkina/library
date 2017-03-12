package home.library.controller;

import home.library.entity.Genre;
import home.library.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Работа со списком авторов
 * методы аналогичны другим контроллерам
 */
@Controller
public class GenreController
{
    @Autowired
    GenreRepository genreRepository;

    @RequestMapping(value = URIPath.PATH_GENRE, method = RequestMethod.GET)
    public String homePage(Model model)
    {
        model.addAttribute("genres", genreRepository.findAll());
        return "genre";
    }

    @RequestMapping(value = URIPath.PATH_GENRE_ADD, method = RequestMethod.POST)
    public String addGenre(@RequestParam(value = "name", required = true) String name)
    {
        Genre genre = new Genre(name);
        genreRepository.save(genre);
        return URIPath.REDIRECT + URIPath.PATH_GENRE;
    }

    @RequestMapping(value = URIPath.PATH_GENRE_EDIT + "/{id}", method = RequestMethod.GET)
    public String editGenrePage(@PathVariable(name = "id") Integer id, Model model)
    {
        Genre genre = genreRepository.findOne(id);
        if (genre == null)
        {
            return URIPath.REDIRECT + URIPath.PATH_GENRE;
        }
        model.addAttribute("genre", genre);
        return "genre_edit";
    }


    @RequestMapping(value = URIPath.PATH_GENRE_EDIT, method = RequestMethod.POST)
    public String editGenre(@RequestParam(value = "id", required = true) Integer id,
                            @RequestParam(value = "name", required = true) String name)
    {
        Genre genre = genreRepository.findOne(id);
        genre.setName(name);
        genreRepository.save(genre);
        return URIPath.REDIRECT + URIPath.PATH_GENRE;
    }

    @RequestMapping(value = URIPath.PATH_GENRE_DELETE, method = RequestMethod.POST)
    public String deleteGenre(@RequestParam(value = "id", required = true) Integer id)
    {
        genreRepository.delete(id);
        return URIPath.REDIRECT + URIPath.PATH_GENRE;
    }


}