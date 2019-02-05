package opp.service;


import java.util.List;

import opp.domain.User;
import opp.service.impl.LoginForm;

public interface UserService {
    public List<User> listAll();

    User create(User user);

    User updateUser(User user);

    User getByUsernameAndPassword(LoginForm loginInfo);

    User getById(Long id);
    
    void deleteBill(Long userId, Long billId);

}
