import java.math.BigDecimal;

public class Institution  {

    Integer institutionId;
    String institutionName;
    String address;
    BigDecimal telephone;
    String superior;

    public Institution(Integer institutionId, String institutionName, String address, BigDecimal telephone, String superior) {
        this.institutionId = institutionId;
        this.institutionName = institutionName;
        this.address = address;
        this.telephone = telephone;
        this.superior = superior;
    }
}
