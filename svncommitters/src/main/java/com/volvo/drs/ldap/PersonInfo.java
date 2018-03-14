package com.volvo.drs.ldap;

public class PersonInfo {

    private String distinguishedName;
    private String firstName;
    private String lastName;
    private String email;
    private String telephoneNumber;

    
    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
        this.email = email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    @Override
    public String toString() {
        return "PersonInfo [firstName=" + getFirstName() 
                + ", lastName=" + getLastName() + ", email=" + getEmail()
                + ", telephoneNumber=" + getTelephoneNumber() + "]";
    }

}
