package storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Προσαρμοσμένη συλλογή (Generic) που αποθηκεύει ΜΟΝΟ αντικείμενα
 * που υλοποιούν τη διεπαφή Storable.
 * * @param <T> Ο τύπος του αντικειμένου (π.χ. User, Vehicle) που πρέπει να είναι Storable.
 */
public class StorableList<T extends Storable> {
    
    // Χρήση Java Collections (εσωτερικά) για τη διαχείριση των στοιχείων
    private List<T> items;

    public StorableList() {
        this.items = new ArrayList<>();
    }

    // Προσθήκη νέου στοιχείου
    public void add(T item) {
        items.add(item);
    }

    // Ανάκτηση στοιχείου βάσει δείκτη (index)
    public T get(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    // Επιστρέφει το μέγεθος της λίστας
    public int size() {
        return items.size();
    }
    
    // Μπορείς να προσθέσεις και άλλες μεθόδους (π.χ. remove, clear) αν τις χρειαστείς!
}