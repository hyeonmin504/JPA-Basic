package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

            //Member m = em.find(Member.class, member.getId());
            //System.out.println("member.getTeam().getClass() = " + m.getTeam().getClass());

            //가짜(프록시) 엔티티 객체에 조회
            Member findMember = em.getReference(Member.class, member.getId());
            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getClass() = " + findMember.getClass());
            System.out.println("emf.getPersistenceUnitUtil().isLoaded(findMember)2 = " + emf.getPersistenceUnitUtil().isLoaded(findMember));
            //username을 검색하기 위해서 검색
            System.out.println("findMember.getUsername() = " + findMember.getUsername());
            //프록시 초기화 여부 확인
            System.out.println("emf.getPersistenceUnitUtil().isLoaded(findMember)3 = " + emf.getPersistenceUnitUtil().isLoaded(findMember));

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

            //객체 타입으로 인해 값이 둘다 바뀌는 예제
            Member member2 = new Member();
            member2.setUsername("member2");
            Address homeAddress = new Address("city", "street", "10000");
            member2.setAddress(homeAddress);
/*
            Member member3 = new Member();
            member3.setAddress(address);
            em.persist(member3);
            //member2.getAddress().setCity("newCity");

            //이 오류를 해결하기 위해 생성자로 새로 만들어 값을 통으로 바꿔야 한다.
            Address newaddress = new Address("newCity", homeAddress.getStreet(), homeAddress.getZipcode());
            member2.setAddress(newaddress);
 */

            //멤버에 의존하는 값 타입들 -> ** 진짜 단순한 경우에만 사용하자 업데이트 칠 경우가 없을 때만 1대다관계로 치환하자
            member2.getFavoriteFoods().add("치킨");
            member2.getFavoriteFoods().add("피자");
            member2.getFavoriteFoods().add("족발");

            member2.getAddressHistory().add(new AddressEntity("old1","street", "10000"));
            member2.getAddressHistory().add(new AddressEntity("old2","street", "10000"));
            em.persist(member2);

            em.flush();
            em.clear();
            System.out.println("JpaMain.main==============================");
            Member findMember2 = em.find(Member.class, member2.getId());

            List<AddressEntity> addressesHistory = findMember2.getAddressHistory();
            for (AddressEntity address : addressesHistory) {
                System.out.println("address.getCity() = " + address.getAddress().getCity());
            }

            Set<String> favoriteFoods = findMember2.getFavoriteFoods();
            for (String favoriteFood : favoriteFoods) {
                System.out.println("favoriteFood = " + favoriteFood);
            }

            //기본적으로 equals를 이용하므로 재정의를 주의하자
            findMember2.getAddressHistory().remove(new AddressEntity("old1","street", "10000"));
            findMember2.getAddressHistory().add(new AddressEntity("newCity","street", "10000"));


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
