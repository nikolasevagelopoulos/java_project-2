package entities.user;


//Εισαγωγή της κλάσης Wallet (από το πακέτο finance, την οποία θα φτιάξουμε μετά)
import finance.Wallet;

public abstract class Customer extends User {
 
 protected String vat; // ΑΦΜ πελάτη
 protected Wallet wallet; // Ο εσωτερικός λογαριασμός (Πορτοφόλι) του πελάτη

 public Customer(String username, String password, String role, String vat) {
     super(username, password, role); // Καλούμε τον constructor της User
     this.vat = vat;
     this.wallet = new Wallet(); // Κάθε πελάτης αποκτά αυτόματα ένα άδειο πορτοφόλι
 }
 
 public Customer() {
     super();
     this.wallet = new Wallet();
 }

 public String getVat() {
     return vat;
 }

 public Wallet getWallet() {
     return wallet;
 }
}