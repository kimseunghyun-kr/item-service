package hello.itemservice.domain.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRepositoryTest {
    ItemRepository itemRepository = new ItemRepository();
    @AfterEach
    void afterEach() {
        itemRepository.clearStore();
    }

    @Test
    void save(){
        //given
        Item item = new Item("itemA", 10000.0, 10);
        //when
        Item savedItem = itemRepository.save(item);
        //then
        Item foundItem = itemRepository.findById(item.getItemId());
        assertThat(savedItem).isEqualTo(foundItem);
    }

    @Test
    void findAll(){
        //given
        Item item1 = new Item("item1", 10000.0, 10);
        Item item2 = new Item("item2", 20000.0, 20);
        itemRepository.save(item1);
        itemRepository.save(item2);
        //when
        List<Item> result = itemRepository.findAll();
        //then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(item1,item2);
    }

    @Test
    void update(){
        //given
        Item item = new Item("item1", 10000.0, 10);
        Item savedItem = itemRepository.save(item);
        Long itemId = savedItem.getItemId();
        //when
        Item updateParam = new Item("item2", 20000.0, 30);
        itemRepository.update(itemId, updateParam);
        Item findItem = itemRepository.findById(itemId);
        //then
        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
    }
}
