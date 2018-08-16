package intern.project.travisalerts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
@RestController

public class AppStatusRequestController {
    @RequestMapping(value ="/app_status")
    public String app_status()
    {
        return "Travis Alerts Application Alive";
    }
}
