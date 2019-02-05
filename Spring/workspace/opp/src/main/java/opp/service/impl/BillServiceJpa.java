package opp.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import opp.dao.BillRepository;
import opp.dao.ItemRepository;
import opp.dao.UserRepository;
import opp.domain.Bill;
import opp.domain.Item;
import opp.domain.User;
import opp.service.BillService;

@Service
public class BillServiceJpa implements BillService {

    @Autowired
    private BillRepository billRepo;

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private ItemRepository itemRepo;

    @Override
    public void deleteItem(Long billId, Long itemId){
    	Bill bill = billRepo.findById(billId).get();
    	Item item = itemRepo.findById(itemId).get();
    	bill.getItemList().remove(item);
    	billRepo.save(bill);
        throw new EntityDeletedException("You just deleted item " + item.getName() + " from bill " + bill.getName() + ".");
    }

    @Override
    public List<Bill> listUsersBills(Long id) {
        Optional<User> possibleUser = userRepo.findById(id);
        if(!possibleUser.isPresent()){
            throw new RequestDeniedException("User with " +id + " does not exist.");
        }
        User user = possibleUser.get();

        return user.getUsersBills();
    }
    
    @Override
    public List<Bill> listUsersSharedBills(Long id) {
    	Optional<User> possibleUser = userRepo.findById(id);
    	if(!possibleUser.isPresent()) {
    		throw new RequestDeniedException("User with " + id + " does not exist.");
    	}
    	User user = possibleUser.get();
    	
    	return user.getOtherUsersBills();
    }

    @Override
    public Bill createBill(Name name, Long userId) {

        Assert.hasText(name.getName(), "You must provide the name of a bill.");
        Assert.isTrue(name.getName().length()>=2 && name.getName().length()<=50,
                "Bill's name must be between 2 and 50 characters long.");

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String todaysDate = df.format(today);

        Bill bill = new Bill(todaysDate, name.getName());

        Optional<User> possUser = userRepo.findById(userId);
        if(!possUser.isPresent()) {
            throw new EntityMissingException(User.class, userId);
        }
        User user = possUser.get();

        Bill savedBill = billRepo.save(bill);
        List<Bill> usersBills = user.getUsersBills();
        if(!usersBills.add(savedBill))
            throw new RequestDeniedException("Could not add bill '" + bill.getName() + "' to user's bills.");
        userRepo.save(user);

        return savedBill;
    }

    @Override
    public Bill updateBill(Bill bill){
        Long billId = bill.getId();
        if(!billRepo.existsById(billId))
            throw new EntityMissingException(Bill.class, billId);
        if (billRepo.existsByIdAndAndDateCreatedNot(billId, bill.getDateCreated())){
            throw new RequestDeniedException("You can not change creation date of a bill.");
        }
        bill.setItemList(billRepo.findById(billId).get().getItemList());
        //if (billRepo.existsByNameAndId(bill.getName(),bill.getId())){
//            throw new RequestDeniedException(
//                    "Bill with name " + bill.getName() + " already exsists"
//            );
//        }
        return billRepo.save(bill);
    }

    @Override
    public Bill shareBill(Long billId, String username){
        Bill bill = fetch(billId);

        if(!userRepo.existsByUsername(username))
            throw new EntityMissingException(User.class, username);
        User user = userRepo.findByUsername(username).get();  // korisnik kojemu želimo troškovnik poslati

        List<Bill> otherUsersBills = user.getOtherUsersBills();

        for(Bill userBill : otherUsersBills) {
            if(userBill.getId() == billId) {
            	throw new RequestDeniedException("You have already shared bill '" + userBill.getName() + "' with " + user.getUsername() + ".");
            }
        }

        List<Bill> myUsersBills = user.getUsersBills();

        for(Bill userBill : myUsersBills) {
            if(userBill.getId() == billId) {
                throw new RequestDeniedException("You cannot share bills with yourself.");
            }
        }

        if(!otherUsersBills.add(bill))
            throw new RequestDeniedException("Could not share bill " + bill.getName() + " with user " + user.getUsername() + ".");

        user.setOtherUsersBills(otherUsersBills);
        if(user != userRepo.save(user))
            throw new RequestDeniedException("Could not share bill " + bill.getName() + " with user " + user.getUsername() + ".");

        return bill;
    }


    public Bill fetch(Long billId) {
        return findById(billId).orElseThrow(
                () -> new EntityMissingException(Bill.class, billId)
        );
    }

    public Optional<Bill> findById(long billId) {
        return billRepo.findById(billId);
    }


}
