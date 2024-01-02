package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
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
            member.setCreateBy("kim");
            member.setCreatedDate(LocalDateTime.now());
            member.setUsername("member1");
            member.changeTeam(team);

            em.persist(member);
            
            //team.getMembers().add(member); // 역방향 연관관계 설정 db에 저장이 안된다
            
            em.flush();
            em.clear();

            Member m = em.find(Member.class, member.getId());
            System.out.println("member.getTeam().getClass() = " + m.getTeam().getClass());
            //가짜(프록시) 엔티티 객체에 조회
            Member findMember = em.getReference(Member.class, member.getId());
            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getClass() = " + findMember.getClass());
            System.out.println("emf.getPersistenceUnitUtil().isLoaded(findMember) = " + emf.getPersistenceUnitUtil().isLoaded(findMember));
            //username을 검색하기 위해서 검색
            System.out.println("findMember.getUsername() = " + findMember.getUsername());
            //프록시 초기화 여부 확인
            System.out.println("emf.getPersistenceUnitUtil().isLoaded(findMember) = " + emf.getPersistenceUnitUtil().isLoaded(findMember));

            //**************************//
            Child child = new Child();
            Child child1 = new Child();

            Parent parent = new Parent();
            parent.addChild(child);
            parent.addChild(child1);

            em.persist(parent);

            em.flush();
            em.clear();

            //orphanRemoval = true -> 리스트에서 빠진 애는 자동 삭제된다
            Parent findParent = em.find(Parent.class, parent.getId());
            findParent.getChildList().remove(0);
            em.remove(findParent); // (orphan = true) + cascade로 다 삭제 = 자식 객체 생명주기를 parent가 관리한다
/*
            List<Member> members = findMember.getTeam().getMembers();

            for (Member m : members) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }

            Movie movie = new Movie();
            movie.setActor("bbb");
            movie.setDirector("aaa");
            movie.setName("바람과함께 사라지다");
            movie.setPrice(20000);

            em.persist(movie);
 */
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
    //지연로딩 프록시
    private  static void printMember(Member member) {
        System.out.println("member.getUsername() = " + member.getUsername());
    }
    private static void printMemberAndTeam(Member member){
        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team.getName() = " + team.getName());

    }
}
