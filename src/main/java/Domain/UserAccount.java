package Domain;

public class UserAccount {
    private String id;
    private String firstName;
    private String lastName;
    private String password;

    public UserAccount(String id, String firstName, String lastName, String password){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public UserAccount(){}

    public String getId(){return id;}
    public String getFirstNAme(){return firstName;}
    public String getLastName(){return lastName;}
    public String getPassword(){return password;}

    public void setId(String id){this.id = id;}
    public void setFirstName(String firstName){this.firstName = firstName;}
    public void setLastName(String lastName){this.lastName = lastName;}
    public void setPassword(String password){this.password = password;}
}
