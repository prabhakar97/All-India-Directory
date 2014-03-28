package in.techbeat.AllIndiaDirectory.model;

/**
 * Created by prabhakar on 25/3/14.
 */
public class NumberDetail {

    private boolean isInvalid;

    private String number;

    private String numberType;

    private String name;

    private String address;

    public NumberDetail() {
        isInvalid = false;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isInvalid() {
        return isInvalid;
    }

    public void setInvalid(boolean isInvalid) {
        this.isInvalid = isInvalid;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Address: " + address + ", Number type: " + numberType + ", Number: " + number;
    }

}
