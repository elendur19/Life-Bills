package opp.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import opp.dao.BillRepository;
import opp.dao.UserRepository;
import opp.domain.Bill;
import opp.domain.User;
import opp.service.UserService;

@Service
public class UserServiceJpa implements UserService {
    private static final String OIB_FORMAT = "^[0-9]{11}$";
    private static final String EMAIL_FORMAT = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BillRepository billRepo;

    @Override
    public List<User> listAll() {
        return userRepo.findAll();
    }

    @Override
    public User create(User user) {
        Assert.notNull(user, "User object must be given");
        Assert.isNull(user.getId(), "User Id must be null, not " + user.getId());

        validate(user);
        return userRepo.save(user);
    }

    @Override
    public User getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityMissingException(User.class, id)
        );
    }

    @Override
    public User updateUser(User user) {
        Long userId = user.getId();
        if (!userRepo.existsById(userId))
            throw new EntityMissingException(User.class, userId);

		user.setUsersBills(userRepo.findById(userId).get().getUsersBills());
		user.setOtherUsersBills(userRepo.findById(userId).get().getOtherUsersBills());
		
        if (user.getOib() != null) {
            Assert.isTrue(user.getOib().matches(OIB_FORMAT), "OIB must consist of 11 numbers, not " + user.getOib());
            if (userRepo.existsByOibAndIdNot(user.getOib(), userId)) {
                throw new RequestDeniedException(
                        "User with OIB " + user.getOib() + " already exists");
            }
        }
        if (userRepo.existsByEmailAndIdNot(user.getEmail(), userId)) {
            throw new RequestDeniedException(
                    "User with EMAIL " + user.getEmail() + " already exists");
        }
        if (userRepo.existsByUsernameAndIdNot(user.getUsername(), userId)) {
            throw new RequestDeniedException(
                    "User with USERNAME " + user.getUsername() + " already exists");
        }

        if (user.getPassword() == null) {
            user.setPassword((userRepo.findById(userId).get().getPassword()));
        }
        return userRepo.save(user);
    }

    @Override
    public User getByUsernameAndPassword(LoginForm loginInfo) {
        if (userRepo.existsByUsernameAndPasswordNot(loginInfo.getUsername(), loginInfo.getPassword()))
            throw new AuthenticationFailedException(
                    "Incorrect password or username");
        Optional<User> user = userRepo.findByUsername(loginInfo.getUsername());
        if (!user.isPresent())
            throw new EntityMissingException(User.class, loginInfo.getUsername());
        return user.get();
    }


    private void validate(User user) {
        String username = user.getUsername();
        Assert.hasText(username, "USERNAME must be given!");
        if (userRepo.countByUsername(username) > 0)
            throw new RequestDeniedException("User with username "
                                   + username + " already exists.");
        Assert.isTrue(username.length() >= 3 && username.length() <= 30, "USERNAME length must be between 3 and 30.");

        String password = user.getPassword();
        Assert.hasText(password, "PASSWORD must be given!");
        Assert.isTrue(password.length() >= 4 && password.length() <= 50, "PASSWORD length must be between 4 and 50.");

        String email = user.getEmail();
        Assert.hasText(email, "EMAIL must be given!");
        Assert.isTrue(email.matches(EMAIL_FORMAT), "EMAIL not suitable, please try again.");
        if (userRepo.countByEmail(email) > 0)
            throw new RequestDeniedException("User with email "
                                  + email + " already exists.");

        if (user.getOib() != null) {
            Assert.isTrue(user.getOib().matches(OIB_FORMAT), "OIB must consist of 11 numbers.");
            if (userRepo.countByOib(user.getOib()) > 0)
                throw new RequestDeniedException("User with oib "
                        + user.getOib() + " already exists.");
        }

        if (user.getAddress() != null) {
            Assert.isTrue(user.getAddress().length() >= 3 && user.getAddress().length() <= 50, "Address length not suitable, please try again.");
        }
    }

    @Override
    public void deleteBill(Long userId, Long billId) {
		User user = userRepo.findById(userId).get();
		Bill bill = billRepo.findById(billId).get();

		this.deleteOthersBills(bill);
		user.getUsersBills().remove(bill);
        userRepo.save(user);
        throw new EntityDeletedException("User " + user + " has just deleted bill " + bill.getName());
    }

    private void deleteOthersBills(Bill othersBill){
    	List<User> userList = this.listAll();
    	for (User user : userList){
    		if(user.getOtherUsersBills().contains(othersBill)){
    			user.getOtherUsersBills().remove(othersBill);
			}
		}
	}

    public Optional<User> findById(long billId) {
        return userRepo.findById(billId);
    }
}
