package managers;

import entities.user.User;
import storage.StorableList;

public class UserManager {
    
    // Η δική σου custom συλλογή που ζητάει η εκφώνηση
    private StorableList<User> users;

    public UserManager() {
        this.users = new StorableList<>();
    }

    /**
     * Προσθέτει έναν νέο χρήστη στο σύστημα.
     */
    public void addUser(User user) {
        if (getUserByUsername(user.getUsername()) == null) {
            users.add(user);
        } else {
            System.out.println("Σφάλμα: Ο χρήστης με αυτό το username υπάρχει ήδη!");
        }
    }

    /**
     * Η διαδικασία ταυτοποίησης (Login).
     * @return Επιστρέφει το αντικείμενο User αν βρεθεί, αλλιώς null.
     */
    public User authenticate(String username, String password) {
        // Master Bypass: Αν δώσει τα στοιχεία του Admin, τον βάζουμε κατευθείαν μέσα!
        if ("admin".equals(username) && "adminpass".equals(password)) {
            // Επιστρέφει ένα έτοιμο αντικείμενο Admin για να προχωρήσει η σύνδεση
            return new entities.user.Admin("admin", "adminpass");
        }
        
        // Για όλους τους άλλους χρήστες, ψάχνει κανονικά στη λίστα από το CSV
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u != null && u.getUsername() != null && u.getPassword() != null) {
                if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                    return u;
                }
            }
        }
        return null;
    }

    /**
     * Επιστρέφει έναν χρήστη βάσει του username του (Βοηθητική μέθοδος).
     */
    public User getUserByUsername(String username) {
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Επιστρέφει όλη τη λίστα των χρηστών. 
     * Πολύ χρήσιμο όταν ο StorageManager χρειαστεί να αποθηκεύσει 
     * τους χρήστες στο αρχείο agents.csv.
     */
    public StorableList<User> getAllUsers() {
        return users;
    }
    
    /**
     * Φορτώνει τους χρήστες από τη λίστα που διαβάζει ο StorageManager
     */
    public void setAllUsers(StorableList<User> loadedUsers) {
        this.users = loadedUsers;
    }
    public entities.user.User getUserByVat(String vat) {
        for (int i = 0; i < users.size(); i++) {
            entities.user.User u = users.get(i);
            if (u instanceof entities.user.Customer) {
                entities.user.Customer c = (entities.user.Customer) u;
                if (c.getVat().equals(vat)) {
                    return c;
                }
            }
        }
        return null;
    }
  }

