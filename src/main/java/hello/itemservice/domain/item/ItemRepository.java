package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {
    private static final Map<Long, Item> store = new HashMap<>(); //static 사용
    private static long sequence = 0L;

    public Item save(Item item){
        item.setItemId(sequence++);
        store.put(item.getItemId(), item);
        return item;
    }
    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll(){
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateParam){
        updateParam.setItemId(itemId);
        store.replace(itemId, updateParam);
    }

    public void delete(Long itemId){
        store.remove(itemId);
    }

    public void clearStore(){
        store.clear();
    }

}
