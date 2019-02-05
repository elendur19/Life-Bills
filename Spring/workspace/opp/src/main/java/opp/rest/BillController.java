package opp.rest;

import java.util.List;
import java.util.Map;

import opp.service.impl.ShareForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import opp.domain.Bill;
import opp.service.BillService;
import opp.service.impl.Name;

@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    private BillService billService;

    //userov id?
    @GetMapping("/{id}")
    public List<Bill> listBills(@PathVariable Long id) {
        return billService.listUsersBills(id);
    }
    
    @GetMapping("/shared/{id}")
    public List<Bill> listSharedBills(@PathVariable Long id) {
    	return billService.listUsersSharedBills(id);
    }

    @PostMapping("/{id}")
    public Bill createBill(@PathVariable Long id,@RequestBody Name name){
        return billService.createBill(name, id);
    }

    @PutMapping("/update/{id}")
    public Bill updateBill(@PathVariable Long id, @RequestBody Bill bill){
        if (!bill.getId().equals(id))
            throw new IllegalArgumentException("Bill ID must be preserved");
        return billService.updateBill(bill);
    }

    @PutMapping("/share/{username}")
    public Bill share(@RequestBody ShareForm sform,@PathVariable String username){  return billService.shareBill(sform.getId(), username);   }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id, @RequestBody Map<String, String> json) {
    	billService.deleteItem(id, Long.valueOf(json.get("itemId")));
    }
}
