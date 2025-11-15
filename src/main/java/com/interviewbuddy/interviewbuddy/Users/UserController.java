package com.interviewbuddy.interviewbuddy.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    public UserRepository userrepository;
    @PostMapping("/Signup")
    public String Signup(@RequestBody User user)
    {
        userrepository.save(user);
        return "Success";
    }
    @PostMapping("/Login")
    public String Login(@RequestBody User user)
    {
        Iterable<User> users = userrepository.findAll();

        for(User i:users)
        {
            if(i.username.equals(user.username) && i.password.equals(user.password))
            {
                return "Success";
            }
        }
        return "Failure";
    }
}
