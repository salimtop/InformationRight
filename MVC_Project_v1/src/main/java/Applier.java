import java.lang.String;

public class Applier {
    private Integer applicationNumber;
    private String name;
    private String middleName;
    private String lastName;
    private Double turkishIdentity;
    private Boolean citizenFlag;
    private Integer applierType;
    private String title;
    private Double telephone;
    private Integer telephoneType;
    private String email;
    private String address;
    private Integer addressType;
    private String fax;

    public Object getByName(String attributeName) {
        switch (attributeName) {
            case "applicationNumber": return applicationNumber;
            case "name": return name;
            case "middleName": return middleName;
            case "lastName": return lastName;
            case "turkishIdentity": return turkishIdentity;
            case "citizenFlag": return citizenFlag;
            case "applierType": return applierType;
            case "LegalPersonTitle": return title;
            case "telephone": return telephone;
            case "telephoneType": return telephoneType;
            case "email": return email;
            case "address": return address;
            case "addressType": return addressType;
            case "fax": return fax;

            default: return null;
        }
    }

    public static String getFieldNames(){
        return "name, middleName, lastName, turkishIdentity, citizenFlag, applierType, LegalPersonTitle, telephone, telephoneType, "+
                "email, address, addressType, fax";
    }

    public void setApplicationNumber(Integer applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTurkishIdentity(Double turkishIdentity) {
        this.turkishIdentity = turkishIdentity;
    }

    public void setCitizenFlag(Boolean citizenFlag) {
        this.citizenFlag = citizenFlag;
    }

    public void setApplierType(Integer applierType) {
        this.applierType = applierType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTelephone(Double telephone) {
        this.telephone = telephone;
    }

    public void setTelephoneType(Integer telephoneType) {
        this.telephoneType = telephoneType;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAddressType(Integer addressType) {
        this.addressType = addressType;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
}
