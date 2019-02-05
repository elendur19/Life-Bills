package opp.service;


import java.util.List;

import opp.domain.Bill;
import opp.service.impl.Name;

public interface BillService {
    List<Bill> listUsersBills(Long id);
    List<Bill> listUsersSharedBills(Long id);
    Bill createBill(Name name, Long id);
    Bill shareBill(Long billId, String username);
    Bill updateBill(Bill bill);
    void deleteItem(Long billId, Long itemId);

}
