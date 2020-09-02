package phonebook;

import java.util.List;

public class Contact implements Comparable<Contact>{
    private String name;
    private String phoneNumber;

    public Contact(String phoneNumber, String name) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Contact(List<String> info) {
        this(info.get(0), info.get(1));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(getName()).append(System.lineSeparator());
        sb.append("Phone: ").append(phoneNumber);
        return sb.toString();
    }

    @Override
    public int compareTo(Contact contact) {
        return getName().compareToIgnoreCase(contact.getName());
    }
}