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
            Member member1 = new Member();
            member1.setName("A");
            member1.setAge(17);
            member1.setRoleType(RoleType.USER);

            Member member2 = new Member();
            member2.setName("B");
            member2.setAge(18);
            member2.setRoleType(RoleType.USER);

            Member member3 = new Member();
            member3.setName("C");
            member3.setAge(19);
            member3.setRoleType(RoleType.ADMIN);

            System.out.println("===================");

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            System.out.println("member.getId() = " + member1.getId());
            System.out.println("member2.getId() = " + member2.getId());
            System.out.println("member3.getId() = " + member3.getId());

            System.out.println("===================");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
