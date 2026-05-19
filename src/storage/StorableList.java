package storage;

import java.util.ArrayList;
import java.util.List;

public class StorableList<T extends Storable> {
    private List<T> items;

    public StorableList() {
        this.items = new ArrayList<>();
    }

    public void add(T item) {
        items.add(item);
    }

    public T get(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    public void set(int index, T item) {
        if (index >= 0 && index < items.size()) {
            items.set(index, item);
        }
    }

    public int size() {
        return items.size();
    }

    public void remove(T item) {
        items.remove(item);
    }
}