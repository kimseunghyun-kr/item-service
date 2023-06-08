package hello.itemservice.domain.item;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Item {
    private Long itemId;
    private String itemName;
    private Double price;
    private Integer quantity;

    public Item(String itemName, Double price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

}
