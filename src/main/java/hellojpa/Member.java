package hellojpa;

import javax.persistence.*;
import java.util.Date;
@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1, allocationSize = 50)// 50개씩 미리 메모리로 땡겨와서 쓰는 것 성능 최적화
public class Member {
    @Id //id만 쓰면 직접 할당
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "MEMBER_SEQ_GENERATOR")
    private Long id;
    @Column(name = "name", nullable = false) //notnull
    private String username;
    private Integer age;
    @Enumerated(EnumType.STRING)//기본은 오디널(0,1,2)로, 타입을 스트링으로 바꿔야 추가했을 때 오류가 안난다
    private RoleType roleType;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    @Lob//varchar을 넘어선 큰 타입
    private String description;

    public Long getId() {
        return id;
    }

    public Member() {
    }

    public void setName(String name) {
        this.username = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
