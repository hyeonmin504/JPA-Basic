package hellojpa.jpql;

import hellojpa.Member;

import javax.persistence.*;

@Entity
@NamedQuery( //named 쿼리
        name = "User.findByUsername",
        query = "select u from User u where u.username = :username"
)
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private int age;

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    @Enumerated(EnumType.STRING)
    private MemberType type;
    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private Groups groups;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Groups getGroups() {
        return groups;
    }

    public void changeGroups(Groups groups) {
        this.groups = groups;
        groups.getUsers().add(this);
    }

    public MemberType getType() {
        return type;
    }

    public void setType(MemberType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                //", groups=" + groups + 양방향 toString은 주의하자
                '}';
    }
}
