package home.library.controller;

import home.library.entity.Book;
import home.library.entity.Client;
import home.library.entity.Ticket;
import home.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Метод для работы с "билетами" на выдачу/сдачу книг
 */
@Controller
public class TicketController
{
    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TitleRepository titleRepository;

    @Autowired
    AuthorRepository authorRepository;

    //Максимальное количество книг за раз
    public static final int MAX_BOOK = 5;
    public ArrayList<Integer> MAX_ROWS_IN_TABLE = new ArrayList<Integer>();

    /**
     * Нажатие кнопки "Выдать/Сдать книги"
     *
     * @param id ID читателя
     * @return Страница читателя с формой выдачи книг, таблицей со взятыми книгами,
     * таблицей с "просроченными" книгами и таблицей с "историей" взятых книг
     */
    @RequestMapping(value = URIPath.PATH_CLIENT_TICKET + "/{id}", method = RequestMethod.GET)
    public String getTickets(@PathVariable(name = "id") Integer id, Model model)
    {
        //Определяем клиента, если не найден - возвращаемся на страницу со списком клиентов
        Client client = clientRepository.findOne(id);
        if (client == null)
        {
            return URIPath.REDIRECT + URIPath.PATH_CLIENT;
        }

        //Заполняем список для формирования таблицы из MAX_BOOK строк
        MAX_ROWS_IN_TABLE.clear();
        for (int i = 0; i < MAX_BOOK; i++)
        {
            MAX_ROWS_IN_TABLE.add(1);
        }
        model.addAttribute("MAX_ROWS_IN_TABLE", MAX_ROWS_IN_TABLE);

        //Получаем "сегодня" для отображения в форме выдачи книг
        Date today = new Date();
        model.addAttribute("dateBegin", today);

        //Получаем список текущих книг
        List<Ticket> currentTicket = client.getCurrentTicket();
        //Формируем "просроченные"
        List<Ticket> overdueTicket = new ArrayList<Ticket>();
        for (Ticket ticket : currentTicket)
        {
            if (!ticket.getClosed() && today.after(ticket.getDateEnd()))
            {
                overdueTicket.add(ticket);
            }
        }
        //из списка текущих удаляем "просроченные"
        currentTicket.removeAll(overdueTicket);
        //История взятых книг
        List<Ticket> history = client.getHistory();

        model.addAttribute("history", history);
        model.addAttribute("client", client);
        model.addAttribute("overdueTicket", overdueTicket);
        model.addAttribute("currentTicket", currentTicket);
        return "client_ticket";
    }

    /**
     * Нжтие кнопки "Оформить"
     *
     * @param _serialNumber массив введенных серйных номеров
     * @param _dateEnd      массив введенных дат сдачи книг
     * @param id            ID читателя
     * @return Страница читателя с формой выдачи книг, таблицей со взятыми книгами,
     * таблицей с "просроченными" книгами и таблицей с "историей" взятых книг
     * @throws ParseException
     */
    @RequestMapping(value = URIPath.PATH_TICKET_POST, method = RequestMethod.POST)
    public String postBook(@RequestParam(value = "serialNumber") String[] _serialNumber,
                           @RequestParam(value = "dateEnd") String[] _dateEnd, @RequestParam(value = "id") Integer id,
                           Model model) throws ParseException
    {
        //Список серийных номеров
        List<String> serialNumber = Arrays.asList(_serialNumber);
        //Список введенных дат
        List<Date> dateEnd = new ArrayList<>();
        //Получаем "Сегодня" для проверки корректности введенных дат
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

        //Проверяем каждую дату из массива
        //Если введена корректно - добавляем в список дат
        for (String date : _dateEnd)
        {
            Matcher matcher = pattern.matcher(date);
            if (!date.isEmpty() && matcher.find())
            {
                Date parseDate = sdf.parse(date);
                if (today.before(parseDate))
                    dateEnd.add(parseDate);
            }
        }

        //Получаем клиента
        Client client = clientRepository.findOne(id);
        //Список взятых книг
        List<Ticket> tickets = new ArrayList<Ticket>();
        //Проверяем запрошенные книги
        for (String num : serialNumber)
        {
            if (!num.isEmpty())
            {
                Book book = bookRepository.findBookBySerialNumber(num);
                //Если книга есть в библиотеке
                if (book != null && book.getAvailable())
                {
                    try
                    {
                        //Создаем "билет" и устанавливаем ему соответствующую дату сдачи
                        Ticket ticket = new Ticket();
                        ticket.setDateEnd(dateEnd.get(serialNumber.indexOf(num)));
                        //Указываем взятую книгу
                        ticket.setBook(book);
                        //И дату, когда взяли
                        ticket.setDateBegin(new Date());
                        //И того, кто взял
                        ticket.setClient(client);
                        //Сохраняем "билет"
                        ticket = ticketRepository.save(ticket);
                        tickets.add(ticket);

                        //В книгу записываем билет, которому она соответствует
                        book.setTicket(ticket);
                        //И делаем недоступной
                        book.setAvailable(false);
                        bookRepository.save(book);

                    } catch (Exception e)
                    {

                    }
                }
            }
        }

        //Если в списке взятых книг есть хотя бы одна
        if (tickets.size() != 0)
        {
            //ПОлучаем список книг, которые на руках у клиента
            List<Ticket> oldCurrentTickets = client.getCurrentTicket();
            //Добавляемн к нему новые
            oldCurrentTickets.addAll(tickets);
            client.setCurrentTicket(oldCurrentTickets);
            client = clientRepository.save(client);
        }

        return URIPath.REDIRECT + URIPath.PATH_CLIENT_TICKET + "/" + client.getId();
    }

    /**
     * Нажатие кнопки "Сдать"
     *
     * @param ticketId   ID билета
     * @param dateClosed дата сдачи книги
     * @param id         ID читателя
     * @return Страница читателя с формой выдачи книг, таблицей со взятыми книгами,
     * таблицей с "просроченными" книгами и таблицей с "историей" взятых книг
     * @throws ParseException
     */
    @RequestMapping(value = URIPath.PATH_TICKET_CLOSE, method = RequestMethod.POST)
    public String closeBook(@RequestParam(value = "ticketId") Integer ticketId,
                            @RequestParam(value = "dateClosed") String dateClosed,
                            @RequestParam(value = "id") Integer id, Model model) throws ParseException
    {
        Client client = clientRepository.findOne(id);

        //Проверяем дату сдачи
        //Если введена корректно - добавляем
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
        Date date = null;
        Matcher matcher = pattern.matcher(dateClosed);
        if (!dateClosed.isEmpty() && matcher.find())
        {
            date = sdf.parse(dateClosed);
        } else
            return URIPath.REDIRECT + URIPath.PATH_CLIENT_TICKET + "/" + client.getId();

        //Берем билет
        Ticket ticket = ticketRepository.findOne(ticketId);
        //Ставим дату, когда сдано
        ticket.setDateClose(date);
        //Закрываем билетик
        ticket.setClosed(true);
        //Сохраняем
        ticket = ticketRepository.save(ticket);
        //Получили книжку из этого билетика
        Book book = bookRepository.findOne(ticket.getBook().getId());
        //сделали доступной
        book.setAvailable(true);
        //взяли историю книги (где кем когда бралась)
        List<Ticket> ticketsHistory = book.getTicketsHistory();
        //если не было истории
        if (ticketsHistory == null)
        {
            //создали
            List<Ticket> tmpList = new ArrayList<Ticket>();
            //и записали
            tmpList.add(ticket);
            book.setTicketsHistory(tmpList);
        }
        //если была
        else
        {
            //добавили к истории
            ticketsHistory.add(ticket);
            book.setTicketsHistory(ticketsHistory);
        }
        bookRepository.save(book);

        //из спсика текущих книг клиента удалили сданную книгу
        client.getCurrentTicket().remove(ticket);
        //получили историю книг клиента
        ticketsHistory = client.getHistory();
        //если не брал книги до этого, создаем и записываем
        if (ticketsHistory == null)
        {
            List<Ticket> tmpList = new ArrayList<Ticket>();
            tmpList.add(ticket);
            client.setHistory(tmpList);
        }
        //если брал, добавляем
        else
        {
            ticketsHistory.add(ticket);
            client.setHistory(ticketsHistory);
        }
        client = clientRepository.save(client);

        return URIPath.REDIRECT + URIPath.PATH_CLIENT_TICKET + "/" + client.getId();

    }


}
