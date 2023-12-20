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
            //영속 = db에 보내기 전에 중간에서 pk, 엔티티, 스냅샷 형태로 1차캐시에 저장되는 공간과 지연 sql저장소
            // jpa가 관리하는 상태 1차 캐시에
            Member member = new Member(200L, "member200");
            Member member1 = em.find(Member.class,150L);
            em.persist(member);
            System.out.println("member1 = " + member1.getName());

            em.flush(); // 쓰기지연 저장소에 정보가 sql에 적용되는 과정

            //member.setName("zzzzzz"); //값을 바꾸는 순간 스냅샷이랑 비교해서 업데이트 쿼리문을 작성해둔다

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
