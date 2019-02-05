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
import opp.dao.ItemCategoryRepository;
import opp.dao.ItemRepository;
import opp.domain.Bill;
import opp.domain.Item;
import opp.domain.ItemCategory;
import opp.service.BillService;
import opp.service.ItemService;

@Service
public class ItemServiceJpa implements ItemService {
    private static final String ITEM_NAME_FORMAT = "^[a-zA-Z ]+[a-zA-Z0-9 _-]{2,20}";
    private static final String ITEM_CATEGORY_NAME_FORMAT = "^[a-zA-Z _-]{2,30}";
    private static final String ITEM_TYPE = "(Expense|Income)";

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private ItemCategoryRepository iCatRepo;

    @Autowired
    private BillRepository billRepo;

    @Autowired
    private BillService billService;

    @Override
    public List<Item> listBillsItems(Long id) {
        Optional<Bill> possibleBill = billRepo.findById(id);
        if(!possibleBill.isPresent()){
            throw new RequestDeniedException("Bill with " +id + " doesn't exist");
        }
        Bill bill = possibleBill.get();

        return bill.getItemList();
    }

    @Override
    public Item createItem(Item item, Long billId) {
        Assert.notNull(item, "Item object must be given!");
        Assert.isNull(item.getId(), "Item's id must be null, not " + item.getId() + ".");

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String todaysDate = df.format(today);

        item.setDateCreated(todaysDate);

        ItemCategory iCat = item.getItemCategory();
        if(iCat != null)
            saveICat(iCat);

        validate(item);
        Item savedItem = itemRepo.save(item);

        Optional<Bill> possBill = billRepo.findById(billId);
        if(!possBill.isPresent())
            throw new EntityMissingException(Bill.class, billId);
        Bill bill = possBill.get();

        List<Item> billsItems = bill.getItemList();
        if(!billsItems.add(savedItem))
            throw new RequestDeniedException("Couldn't add item" + item.getName() + " to user's bills.");
        billService.updateBill(bill);

        return savedItem;

    }
    @Override
    public Item updateItem(Item item){
        Long itemId = item.getId();
        if(!itemRepo.existsById(itemId)) {
        	System.out.println("Item with id" + itemId + " doesn't exist.");
        	throw new EntityMissingException(Item.class, itemId);
        }
        if (itemRepo.existsByIdAndDateCreatedNot(itemId, item.getDateCreated())){
        	System.out.println("Item's date created can not be changed.");
            throw new RequestDeniedException("Can't change date created");
        }

        return itemRepo.save(item);
    }

    public void validate(Item item){

        String name = item.getName();
        Assert.hasText(name, "Item's name must be given.");
        Assert.isTrue((name.matches(ITEM_NAME_FORMAT)) , "The given item's name is not valid name.");

        Assert.notNull(item.getItemCategory(),"Item has to have a category.");

        String itemCategoryName = item.getItemCategory().getName();
        Assert.hasText(itemCategoryName, "Item has to have a category.");
        Assert.isTrue(itemCategoryName.matches(ITEM_CATEGORY_NAME_FORMAT),
                "The given item's category name is not a valid name.");

        String itemType = item.getItemType();
        Assert.hasText(itemType, "Item has to have a defined type.");
        Assert.isTrue(itemType.matches(ITEM_TYPE), "The given item type is not a valid type.");

        String itemComment = item.getAdditionalComment();
        if (itemComment != null) {
            Assert.isTrue(itemComment.length()>=2 && itemComment.length()<=50, "The comment must be between 2 and 50 characters long)");
        }
    }

    public void saveICat(ItemCategory iCat){
        iCatRepo.save(iCat);
    }
}
