package ro.ne8.resourceserver.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.ne8.resourceserver.dto.Bar;

@Controller
public class BarController {
    private final Log LOGGER = LogFactory.getLog(BarController.class);


    // API - read
    @PreAuthorize("#oauth2.hasScope('read') and hasRole('USER')")
    @RequestMapping(method = RequestMethod.GET, value = "/bars/{id}")
    @ResponseBody
    public Bar findById(@PathVariable final long id) {
        final String principal = this.getPrincipal();
        this.LOGGER.info("User used to access controller:" + principal);
        return new Bar(Long.parseLong("2"), "name");
    }

    // API - write
    @PreAuthorize("#oauth2.hasScope('bar') and #oauth2.hasScope('write') and hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST, value = "/bars")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Bar create(@RequestBody final Bar bar) {
        bar.setId(Long.parseLong("2"));
        return bar;
    }

    private String getPrincipal() {
        String userName = null;
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

}