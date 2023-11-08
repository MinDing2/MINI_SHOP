package jpabook.jpashop.domain;


import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FtemRepository {
    private final Map<Long, Ftem> store = new HashMap<>();
    private long sequence = 0L;

    public Ftem save(Ftem item){
        item.setFd(++sequence);
        store.put(item.getFd(), item);
        return item;
    }

    public Ftem findById(Long Fd){
        return store.get(Fd);
    }
}
