package home.library.controller;

import home.library.entity.Book;
import home.library.entity.Client;
import home.library.entity.Ticket;
import home.library.entity.Title;
import home.library.repository.BookRepository;
import home.library.repository.TicketRepository;
import home.library.repository.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Работа со списком книг
 */
@Controller
public class BookController
{
    @Autowired
    BookRepository bookRepository;

    @Autowired
    TitleRepository titleRepository;

    @Autowired
    TicketRepository ticketRepository;

    /**
     * Получаем список книг и произведений
     *
     * @return Страница со списком книг в библиотеке и формой на добавление новой книги
     */
    @RequestMapping(value = URIPath.PATH_BOOK, method = RequestMethod.GET)
    public String homePage(Model model)
    {
        model.addAttribute("books", bookRepository.findAll());
        //список произведений необходим для формы добавления книги
        model.addAttribute("titles", titleRepository.findAll());
        return "book";
    }

    /**
     * Нажатие на кнопку "Добавить книгу"
     *
     * @param serialNumber серийный номер книги
     * @param title        заголовок(произведение) книги
     * @return Страница со списком книг в библиотеке и формой на добавление новой книги
     */
    @RequestMapping(value = URIPath.PATH_BOOK_ADD, method = RequestMethod.POST)
    public String addBook(@RequestParam(value = "serialNumber", required = true) String serialNumber,
                          @RequestParam(value = "title", required = true) Title title)
    {
        Book book = new Book(title, serialNumber);
        book.setAvailable(true);
        bookRepository.save(book);

        return URIPath.REDIRECT + URIPath.PATH_BOOK;
    }

    /**
     * Нажатие на кнопку "Изменить"
     *
     * @param id ID книги
     * @return Страница с формой редактирования книги
     */
    @RequestMapping(value = URIPath.PATH_BOOK_EDIT + "/{id}", method = RequestMethod.GET)
    public String editBookPage(@PathVariable(name = "id") Integer id, Model model)
    {
        Book book = bookRepository.findOne(id);
        if (book == null)
        {
            return URIPath.REDIRECT + URIPath.PATH_BOOK;
        }
        model.addAttribute("book", book);
        model.addAttribute("titles", titleRepository.findAll());
        return "book_edit";

    }

    /**
     * Нажатие на кнопку "Изменить" на странице редактирования книги
     *
     * @param id           ID книги
     * @param serialNumber серийный номер
     * @param title        заголовок(произведение)
     * @return Страница со списком книг в библиотеке и формой на добавление новой книги
     */
    @RequestMapping(value = URIPath.PATH_BOOK_EDIT, method = RequestMethod.POST)
    public String editBook(@RequestParam(value = "id", required = true) Integer id,
                           @RequestParam(value = "serialNumber", required = true) String serialNumber,
                           @RequestParam(value = "title", required = true) Title title)
    {
        Book book = bookRepository.findOne(id);
        book.setSerialNumber(serialNumber);
        book.setTitle(title);
        bookRepository.save(book);
        return URIPath.REDIRECT + URIPath.PATH_BOOK;
    }

    /**
     * Нажатие на кнопку "Удалить"
     *
     * @param id ID книги
     * @return Страница со списком книг в библиотеке и формой на добавление новой книги
     */
    @RequestMapping(value = URIPath.PATH_BOOK_DELETE, method = RequestMethod.POST)
    public String deleteBook(@RequestParam(value = "id", required = true) Integer id)
    {
        Book book = bookRepository.findOne(id);
        bookRepository.delete(id);
        return URIPath.REDIRECT + URIPath.PATH_BOOK;
    }

    /**
     * Нажатие на кнопку "КТО БРАЛ?!"
     *
     * @param id ID книги
     * @return Страница со списком клиентов когда-либо бравших эту книгу
     */
    @RequestMapping(value = URIPath.PATH_BOOK_CLIENT + "/{id}", method = RequestMethod.GET)
    public String clientBook(@PathVariable(name = "id") Integer id, Model model)
    {
        //Список "билетов" в которых есть кинга
        List<Ticket> tickets = ticketRepository.findTicketByBook(id);
        //список клиентов, бравших книгу
        List<Client> clients = new ArrayList<Client>();
        for (Ticket ticket : tickets)
        {
            clients.add(ticket.getClient());
        }
        Book book = bookRepository.findOne(id);
        model.addAttribute("book", book);
        model.addAttribute("clients", clients);

        return "book_client";
    }

}
