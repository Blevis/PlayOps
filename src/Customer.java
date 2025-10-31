public class Customer {

    // PROPERTIES
    private static int nextId = 1;
    private final int id;
    private String name;
    private String lastName;
    private String email;
    private String address;

    // CONSTRUCTOR
    public Customer(String name, String lastName, String email, String address) {
        this.id = nextId++;
        this.name = name;
        this.lastName = lastName;
        setEmail(email);
        this.address = address;
    }

    // GET/SET METHODS
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (isValidEmail(email)){
            this.email = email;
        } else {
            throw new IllegalArgumentException("Error: Invalid email format.\nEnsure email space is not empty, and follow a valid format of address@domain.com");
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // OTHER METHODS
    private boolean isValidEmail(String email){
        return email != null  && email.contains("@") && email.contains(".");
    }


    // TO STRING METHOD
    @Override
    public String toString(){
        return "[" + id + "] " + name + " " + lastName + " | " + email + " | " + address;
    }

}
