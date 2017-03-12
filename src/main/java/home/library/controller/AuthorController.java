package home.library.controller;

import home.library.entity.Author;
import home.library.repository.AuthorRepository;
import home.library.util.myMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Работа со списком авторов
 */
@Controller
public class AuthorController
{
    @Autowired
    AuthorRepository authorRepository;
    /**
     * Получаем список авторов
     * @return Страница со списком авторов и формой на добавление нового автора
     */
    @RequestMapping(value = URIPath.PATH_AUTHOR, method = RequestMethod.GET)
    public String homePage(Model model)
    {
        model.addAttribute("authors",authorRepository.findAll());
        return "author";
    }

    /**
     * Нажатие кнопки "Создать автора"
     * @param lastName Фамилия
     * @param firstName Имя
     * @param patronymic Отчество
     * @return Страница со списком авторов и формой на добавление нового автора
     */
    @RequestMapping(value = URIPath.PATH_AUTHOR_ADD,method = RequestMethod.POST)
    public String addAuthor(@RequestParam(value = "lastName",required = true)String lastName,
                            @RequestParam(value = "firstName",required = true)String firstName,
                            @RequestParam(value = "patronymic",required = true)String patronymic)
    {
        //Проверяем корректность введенных данных
        myMatcher m = new myMatcher();
        if(!m.matchPersonalInfo(lastName, firstName, patronymic))
            return URIPath.REDIRECT+URIPath.PATH_AUTHOR;
        //если все ок - добавляем
        Author author = new Author(lastName, firstName, patronymic);
        authorRepository.save(author);

        return URIPath.REDIRECT + URIPath.PATH_AUTHOR;
    }

    /**
     * Нажатие на кнопку "Удалить"
     * @param id ID автора
     * @return Страница со списком авторов и формой на добавление нового автора
     */
    @RequestMapping(value = URIPath.PATH_AUTHOR_DELETE, method = RequestMethod.POST)
    public String deleteAuthor(@RequestParam(value="id",required = true)Integer id)
    {
        authorRepository.delete(id);
        return URIPath.REDIRECT + URIPath.PATH_AUTHOR;
    }

    /**
     * Нажатие на кнопку "Изменить"
     * @param id ID автора
     * @param
     * @return Страница с формой редактирования данных автора
     */
    @RequestMapping(value = URIPath.PATH_AUTHOR_EDIT+"/{id}",method = RequestMethod.GET)
    public String editAuthorPage(@PathVariable(name = "id")Integer id, Model model)
    {
        Author author = authorRepository.findOne(id);
        if(author==null)
        {
            return URIPath.REDIRECT+URIPath.PATH_AUTHOR;
        }
        model.addAttribute("author", author);
        return "author_edit";
    }

    /**
     * Нажатие на кнопку "Подтвердить изменения"
     * @param id ID автора
     * @param lastName Фамилия
     * @param firstName Имя
     * @param patronymic Отчество
     * @return
     */
    @RequestMapping(value = URIPath.PATH_AUTHOR_EDIT,method = RequestMethod.POST)
    public String editAuthor(@RequestParam(value = "id",required = true)Integer id,
                             @RequestParam(value = "lastName",required = true)String lastName,
                             @RequestParam(value = "firstName",required = true)String firstName,
                             @RequestParam(value = "patronymic",required = true)String patronymic)
    {
        //Проверяем на корректность, если все ок - сохраняем
        myMatcher m = new myMatcher();
        if(!m.matchPersonalInfo(lastName, firstName, patronymic))
            return URIPath.REDIRECT+URIPath.PATH_AUTHOR;
        Author author = authorRepository.findOne(id);
        author.getPersonInfo().setLastName(lastName);
        author.getPersonInfo().setFirstName(firstName);
        author.getPersonInfo().setPatronymic(patronymic);
        authorRepository.save(author);
        return URIPath.REDIRECT + URIPath.PATH_AUTHOR;
    }


}
