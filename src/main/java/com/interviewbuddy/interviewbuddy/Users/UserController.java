package com.interviewbuddy.interviewbuddy.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
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
    @PostMapping("/GenerateQuestion")
    public String GenerateQuestion(@RequestParam String question_type)
    {
        Client client = Client.builder().apiKey("AIzaSyASINHTedk1AsXuU26EYj_eMPvgQCKctRY").build();
        String prompt = "Imagine you are an Interviewer,and are supposed to take interview on"+question_type+".Generate a question as part of this interview,and send it back as a response.Just send me the question withou any additional explanation.JUST THE QUESTION";
    GenerateContentResponse response =
        client.models.generateContent(
            "gemini-2.5-flash",
            prompt,
            null);

    return response.text();
    }
    @PostMapping("/ValidateAnswer")
    public String ValidateAnswer(@RequestBody Response Response)
    {
        Client client = Client.builder().apiKey("AIzaSyASINHTedk1AsXuU26EYj_eMPvgQCKctRY").build();
        String prompt = "Imagine you are an Interviewer,and are supposed to validate the response provided by the user for a given question.Here is the question:"+Response.question+"and the answer provided:"+Response.answer+".Give your feedback on this answer provided by the user,and tell them whether the answer is right or wrong,and why it is right/wrong. Just provide the response as right/wrong,and the explanation behind this validation.DONT PROVIDE ANY ADDITIONAL INFORMATION";
        GenerateContentResponse response =
        client.models.generateContent(
            "gemini-2.5-flash",
            prompt,
            null);
return response.text();
    }

}
