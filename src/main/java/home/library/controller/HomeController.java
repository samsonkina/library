package home.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController
{
    @RequestMapping(value = URIPath.PATH_HOME, method = RequestMethod.GET)
    public String homePage()
    {
        return "home";
    }
}
