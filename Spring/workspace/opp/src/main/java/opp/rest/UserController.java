package opp.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import opp.domain.User;
import opp.service.UserService;
import opp.service.impl.LoginForm;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public List<User> listOsoba() { return userService.listAll(); }

    @PostMapping("/register")
    public User createUser(@RequestBody User user){
        return userService.create(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        if (!user.getId().equals(id))
            throw new IllegalArgumentException("User ID must be preserved");
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) { return userService.getById(id);}

    @PostMapping("/login")
    public User login(@RequestBody LoginForm loginInfo){
        return userService.getByUsernameAndPassword(loginInfo);
    }
    
    @DeleteMapping("/{id}")
    public void deleteBill(@PathVariable Long id, @RequestBody Map<String, String> json){
    	userService.deleteBill(id, Long.valueOf(json.get("billId")));
    }
}
