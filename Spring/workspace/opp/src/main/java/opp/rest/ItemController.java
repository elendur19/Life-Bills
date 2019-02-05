package opp.rest;

import opp.domain.Item;
import opp.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    // bilov id?
    @GetMapping("/{id}")
    public List<Item> listItems(@PathVariable Long id) {
        return itemService.listBillsItems(id);
    }

    @PostMapping("/add/{id}")
    public Item createItem(@RequestBody Item item, @PathVariable Long id) {
        return itemService.createItem(item, id);
    }

    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Item item) {
        if (!item.getId().equals(id))
            throw new IllegalArgumentException("Item ID must be preserved");
        return itemService.updateItem(item);
    }
}

