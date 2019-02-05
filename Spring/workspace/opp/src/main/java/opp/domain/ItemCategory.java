package opp.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

@Entity
public class ItemCategory {

    @Id
    @Size(min = 2, max = 25)
    private String name;

    @OneToOne
    private ItemCategory subCategory;

    public ItemCategory() {}

    public ItemCategory(String name){
        this.name = name;
        this.subCategory = null;
    }

    public ItemCategory(String name, ItemCategory subCategory) {
        this.name = name;
        this.subCategory = subCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(ItemCategory subCategory) {
        this.subCategory = subCategory;
    }
}