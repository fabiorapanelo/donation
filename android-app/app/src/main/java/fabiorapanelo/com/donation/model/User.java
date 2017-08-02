package fabiorapanelo.com.donation.model;

/**
 * Created by fabio on 29/07/2017.
 */

public class User {

    private Long id;
    private String name;
    private String username;
    private boolean receiveDonations;
    private boolean verified;
    private String password;
    private String securePassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isReceiveDonations() {
        return receiveDonations;
    }

    public void setReceiveDonations(boolean receiveDonations) {
        this.receiveDonations = receiveDonations;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurePassword() {
        return securePassword;
    }

    public void setSecurePassword(String securePassword) {
        this.securePassword = securePassword;
    }
}
