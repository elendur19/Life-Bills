package opp.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import org.springframework.util.Assert;

@Entity
public class Bill {

    @Id
    @GeneratedValue
    private Long id;

    private String dateCreated;


    @Size(min = 2, max = 50)
    private String name;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Item> itemList;

    public Bill(String dateCreated, @Size(min = 2, max = 50) String billName) {

        Assert.hasText(billName, "Bill's name must be given.");
        Assert.isTrue((billName.length()>=2 && billName.length()<=50), "the length of the bill name must be between 2 and 50");

        this.dateCreated = dateCreated;
        this.name = billName;
    }

    public Bill() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}