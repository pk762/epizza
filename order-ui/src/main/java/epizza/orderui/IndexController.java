package epizza.orderui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class IndexController {

    @Value("${api.baseUrl}")
    private String apiHost;

    @ModelAttribute("apiHost")
    public String apiHost() {
        return apiHost;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}
