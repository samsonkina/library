package home.library.controller;

import home.library.entity.Client;
import home.library.repository.AuthorRepository;
import home.library.repository.ClientRepository;
import home.library.repository.TicketRepository;
import home.library.repository.TitleRepository;
import home.library.util.myMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Работа со списком клиентов
 */
@Controller
public class ClientController
{
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    TitleRepository titleRepository;

    /**
     * Получаем список читателей
     * @return Страница со списком читателей и формой на добавление нового читателя
     */
    @RequestMapping(value = URIPath.PATH_CLIENT, method = RequestMethod.GET)
    public String homePage(Model model)
    {
        model.addAttribute("clients", clientRepository.findAll());
        return "client";

    }
    /**
     * Нажатие на кнопку "Добавить клиента"
     * @param lastName Фамилия
     * @param firstName Имя
     * @param patronymic Отчество (не обязатеьно)
     * @param birth дата рождения
     * @param phone номер телефона
     * @param address адрес
     * @return Страница со списком читателей и формой на добавление нового читателя
     */
    @RequestMapping(value = URIPath.PATH_CLIENT_ADD, method = RequestMethod.POST)
    public String addClient(@RequestParam(value = "lastName", required = true) String lastName,
                            @RequestParam(value = "firstName", required = true) String firstName,
                            @RequestParam(value = "patronymic", required = false) String patronymic,
                            @RequestParam(value = "birth", required = true) String birth,
                            @RequestParam(value = "phone", required = true) String phone,
                            @RequestParam(value = "address", required = true) String address)
    {
        //проверяем корректность данных
        myMatcher m = new myMatcher();
        if(!m.matchPersonalInfo(lastName, firstName, patronymic))
            return URIPath.REDIRECT+URIPath.PATH_CLIENT;
        if(!m.matchPhone(phone))
            return URIPath.REDIRECT+URIPath.PATH_CLIENT;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try
        {
            date = simpleDateFormat.parse(birth);
        } catch (ParseException e)
        {
            date = null;
        }
        //если все ок - записываем
        Client client = new Client(firstName, lastName, patronymic, date, phone, address);
        clientRepository.save(client);
        return URIPath.REDIRECT + URIPath.PATH_CLIENT;
    }

    /**
     * Нажатие на кнопку "Изменить"
     * @param id ID клиента
     * @return Страница с формой изменения информации о клиенте
     */
    @RequestMapping(value = URIPath.PATH_CLIENT_EDIT + "/{id}", method = RequestMethod.GET)
    public String editClientPage(@PathVariable(name = "id") Integer id, Model model)
    {
        Client client = clientRepository.findOne(id);
        if (client == null)
        {
            return URIPath.REDIRECT + URIPath.PATH_CLIENT;
        }
        model.addAttribute("client", client);
        return "client_edit";
    }

    /**
     * Нажатие на кнопку "Подтвердить изменения"
     * @param lastName Фамилия
     * @param firstName Имя
     * @param patronymic Отчество (не обязатеьно)
     * @param birth дата рождения
     * @param phone номер телефона
     * @param address адрес
     * @return Страница со списком читателей и формой на добавление нового читателя
     */
    @RequestMapping(value = URIPath.PATH_CLIENT_EDIT, method = RequestMethod.POST)
    public String editClient(@RequestParam(value = "id", required = true) Integer id,
                             @RequestParam(value = "lastName", required = true) String lastName,
                             @RequestParam(value = "firstName", required = true) String firstName,
                             @RequestParam(value = "patronymic", required = false) String patronymic,
                             @RequestParam(value = "birth", required = true) String birth,
                             @RequestParam(value = "phone", required = true) String phone,
                             @RequestParam(value = "address", required = true) String address) throws ParseException
    {
//проверяем корректность
        myMatcher m = new myMatcher();
        if(!m.matchPersonalInfo(lastName, firstName, patronymic))
            return URIPath.REDIRECT+URIPath.PATH_CLIENT;
        if(!m.matchPhone(phone))
            return URIPath.REDIRECT+URIPath.PATH_CLIENT;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try
        {
            date = simpleDateFormat.parse(birth);
        } catch (ParseException e)
        {
            date = null;
        }
        Client client = clientRepository.findOne(id);
        client.getPersonInfo().setLastName(lastName);
        client.getPersonInfo().setFirstName(firstName);
        client.getPersonInfo().setPatronymic(patronymic);
        client.getPersonInfo().setBirth(date);
        client.setPhone(phone);
        client.setAddress(address);
        clientRepository.save(client);
        return URIPath.REDIRECT + URIPath.PATH_CLIENT;
    }

    /**
     * Нажатие на кнопку "Удалить"
     * @param id ID читателя
     * @return Страница со списком читателей и формой на добавление нового читателя
     */
    @RequestMapping(value = URIPath.PATH_CLIENT_DELETE, method = RequestMethod.POST)
    public String deleteClient(@RequestParam(value = "id", required = true) Integer id)
    {
        clientRepository.delete(id);
        return URIPath.REDIRECT + URIPath.PATH_CLIENT;
    }



}
