package opp.service;

import opp.domain.Item;

import java.util.List;

public interface ItemService {
    List<Item> listBillsItems(Long id);
    Item createItem(Item item, Long id);
    Item updateItem(Item item);
}
