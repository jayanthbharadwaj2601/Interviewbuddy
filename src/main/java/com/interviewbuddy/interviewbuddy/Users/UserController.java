package com.interviewbuddy.interviewbuddy.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.interviewbuddy.interviewbuddy.Questions.QuestionRepository;
import com.interviewbuddy.interviewbuddy.Questions.Questions;
@RestController
public class UserController {
    @Autowired
    public UserRepository userrepository;
    @Autowired
    public QuestionRepository questionrepository;
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
    public String GenerateQuestion(@RequestBody Questions question_type)
    {
        String api_key = System.getenv("GEMINI_API_KEY");
        System.out.println("API KEY: "+api_key);
        Iterable<Questions> questions= questionrepository.findAll();
        String prompt = "Imagine you are an Interviewer,and are supposed to take interview on"+question_type.question+".Generate a question as part of this interview,and send it back as a response.Just send me the question withou any additional explanation.JUST THE QUESTION,and does not exceed 200 characters.Ensure that this question is unique and is not part of the following questions listed below:";
        for(Questions q:questions)
        {   if(q.userid==question_type.userid)
            prompt+=q.question+".";
        }
        Client client = Client.builder().apiKey(api_key).build();;
        GenerateContentResponse response =
        client.models.generateContent(
            "gemini-2.5-flash",
            prompt,
            null);
        Questions generated_question = new Questions();
        generated_question.question = response.text();
        generated_question.userid = question_type.userid;
        questionrepository.save(generated_question);
    return response.text();
    }
    @PostMapping("/ValidateAnswer")
    public String ValidateAnswer(@RequestBody Response Response)
    {
        String key = System.getenv("GEMINI_API_KEY");
        Client client = Client.builder().apiKey(key).build();
        String prompt = "Imagine you are an Interviewer,and are supposed to validate the response provided by the user for a given question.Here is the question:"+Response.question+"and the answer provided:"+Response.answer+".Give your feedback on this answer provided by the user,and tell them whether the answer is right or wrong,and why it is right/wrong. Just provide the response as right/wrong,and the explanation behind this validation.DONT PROVIDE ANY ADDITIONAL INFORMATION.Your response should be as follows: Line 1:True/False,Line 2:Explanation";
        GenerateContentResponse response =
        client.models.generateContent(
            "gemini-2.5-flash",
            prompt,
            null);
        boolean response1;
        if (response.text().charAt(0)=='T')
        {
            response1  = true;
        }
        else{
            response1=false;
        }

        Iterable<Questions> responses=questionrepository.findAll();
        for(Questions q:responses)
        {
            if(q.question.equals(Response.question) && q.userid==Response.userid)
            {
                q.response=response1;
                questionrepository.save(q);
            }
        }
        return response.text();
    }
    @PostMapping("/Feedback")
    public String Feedback(@RequestParam String type)
    {
        String prompt="I'll give you a list of questions along with whether the user has provided correct feedback for those questions.But you only have to provide feedback to the questions which match this topic:"+type+".Here is the list:";
        String key = System.getenv("GEMINI_API_KEY");
        Client client = Client.builder().apiKey(key).build();
        Iterable<Questions> responses=questionrepository.findAll();
        for(Questions q:responses)
        {
            
            prompt+="Question:"+q.question+",Correctness:"+q.response+".";
        }
        GenerateContentResponse response =client.models.generateContent(
            "gemini-2.5-flash",
            prompt,
            null);
        return response.text();
        
    }

}
