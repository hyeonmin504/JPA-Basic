package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Member extends BaseEntitiy{
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    //@Column(name = "TEAM_ID")
    //private Long teamid;

    //(fetch = FetchType.EAGER -> 한번에 즉시로딩 함 ** 실무에선 지양
    @ManyToOne(fetch = FetchType.LAZY) // team을 프록시 객체로 지연로딩 함
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @OneToOne // 1대1 매
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;
    //기간 Period
    @Embedded
    private Period period;
    //주소
    @Embedded
    private Address address;

    //값 타입 컬렉션은 영속성 전이 + 고아 객체 제거 기능 필수로 가짐, 기본이 지연로딩
    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns =
    @JoinColumn(name = "MEMBER_ID")
    )
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

    //추적이 필요한 address같은 경우는 entity로 작성
    //@ElementCollection
    //@CollectionTable(name = "ADDRESS", joinColumns =
    //@JoinColumn(name = "MEMBER_ID"))
    //private List<Address> addressHistory = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID")
    private List<AddressEntity> addressHistory = new ArrayList<>();

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Address getAddress() {
        return address;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

    public void setAddressHistory(List<AddressEntity> addressHistory) {
        this.addressHistory = addressHistory;
    }

    public List<AddressEntity> getAddressHistory() {
        return addressHistory;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    public void changeTeam(Team team) {
        this.team = team;
        //team.getMembers().add(this); //양방향 연관관계 주입 한쪽만 설정해도 양쪽에 자동으로 설정
    }
}
