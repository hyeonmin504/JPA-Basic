package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /*
            //영속 = db에 보내기 전에 중간에서 pk, 엔티티, 스냅샷 형태로 1차캐시에 저장되는 공간과 지연 sql저장소
            // jpa가 관리하는 상태 1차 캐시에
            Member member = new Member(208L, "member208");
            em.persist(member);

            Member member1 = em.find(Member.class,208L);
            System.out.println("member1 = " + member1.getName());

            //member.setName("zzzzzz"); //값을 바꾸는 순간 스냅샷이랑 비교해서 업데이트 쿼리문을 작성해둔다

            System.out.println("===================");
             */

            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);


            member.changeTeam(team);
            //team.getMembers().add(member); // 역방향 연관관계 설정 db에 저장이 안된다

            System.out.println("team.getMembers().add(member) = " + team.getMembers().add(member));

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            List<Member> members = findMember.getTeam().getMembers();

            for (Member m : members) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
