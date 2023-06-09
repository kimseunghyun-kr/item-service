package hello.itemservice.web.item;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
@Slf4j
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

////    requestParam only works in following scenarios ?id=abc
//    @GetMapping("/{itemId}")
//    public String Item(Model model, @RequestParam Long itemId){
//        Item item = itemRepository.findById(itemId);
//        model.addAttribute("item" , item);
//        return "basic/item";
//    }

//    PathVariable is for one variable only
@GetMapping("/{itemId}")
public String item(@PathVariable Long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "basic/item";
}

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addFormV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {

        Item item = new Item(itemName, price/1.0, quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addFormV2(@ModelAttribute("item") Item item, Model model) {
        itemRepository.save(item);
        //model.addAttribute("item", item); //automatically added by modelattribute. able to skip
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

    /**
     * @ModelAttribute skipped
     * model.addAttribute(item) automatically added
     */
//    @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

//    need to redirect to avoid repeated post requests upon refresh
//    PRG(Post/Redirect/Get) pattern -> intro to PRG pattern
//    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getItemId(); //this is undesired, should this result in a String with a space, non supported langs -> is a cause for tons of bugs
    }
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getItemId());
        redirectAttributes.addAttribute("status", true);
//        attributes not directly present in the return viewName String will be appended as a queryparameter,
//        that can be utilised by the view later, through 'param. xxxx' -> exclusive(?) to thymeleaf
        return "redirect:/basic/items/{itemId}";
    }

//    send data of existing item to thymeleaf view for editForm view
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping ("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }


    @GetMapping("/{itemId}/delete")
    public String deleteForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/deleteForm";
    }

    @PostMapping ("/{itemID}/delete")
    public String delete(@PathVariable Long itemID, Model model) {
        itemRepository.delete(itemID);
        String ret = this.items(model);
        log.info(ret);
        return "redirect:/" + ret;
    }


    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testA", 10000.0, 10));
        itemRepository.save(new Item("testB", 20000.0, 20));
    }
}
