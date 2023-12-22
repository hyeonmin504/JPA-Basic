package hellojpa;

import javax.persistence.*;
import java.util.Date;
@Entity
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    //@Column(name = "TEAM_ID")
    //private Long teamid;

    @ManyToOne // 다대1 연관관계 맵핑
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @OneToOne // 1대1 매
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

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
