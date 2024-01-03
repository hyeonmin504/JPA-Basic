package hellojpa;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable // 불변객체 = 생성자 레벨에서만 값을 변경하고 setter을 제거하거나 private로 생성한다.
public class Address {

    public Address() {}
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
    private String city;
    private String street;
    private String zipcode;


    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipcode() {
        return zipcode;
    }

    //인베디드 타입 비교를 위한 equals 오버라이딩
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) &&
                Objects.equals(street, address.street) &&
                Objects.equals(zipcode, address.zipcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, zipcode);
    }
}
