package hellojpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Locker {
    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToOne(mappedBy = "locker") //양방향 1대1
    private Member member;
}
