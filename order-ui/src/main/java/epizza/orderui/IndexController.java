package epizza.orderui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class IndexController {

    private ApiProperties apiProperties;

    public IndexController(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    @ModelAttribute("apiHost")
    public String apiHost() {
        return String.valueOf(apiProperties.getBaseUri());
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}
