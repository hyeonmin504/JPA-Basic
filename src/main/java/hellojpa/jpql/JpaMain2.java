package hellojpa.jpql;

import hellojpa.Member;
import hellojpa.Team;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class JpaMain2 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Groups groups = new Groups();
            groups.setName("group");
            em.persist(groups);

            for (int i=0 ; i<4 ; i++){
                User user = new User();
                user.setUsername("user" + i);
                user.setAge(i);
                user.setType(MemberType.ADMIN);

                user.changeGroups(groups);

                em.persist(user);
            }

            em.flush();
            em.clear();


            //스칼라 타입 프로젝션 = select 절에 여러 값을 조회할 대상을 지정하는 것
            List<MemberDTO> result = em.createQuery("select new hellojpa.jpql.MemberDTO(u.username, u.age) from User u", MemberDTO.class)
                    .getResultList();

            MemberDTO memberDTO = result.get(0);
            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());

            //페이징 + 조인 ,inner join = 관계가 있는 것만 가져옴, left join = group이 없어도 그냥 조인 시킴 ,
            //on = 조건절 , query2 = 관계없음 엔티티 관계 조간
            String query = "select u from User u left join u.groups g on g.name = 'group1'";
            String query2 = "select u from User u left join Groups g on u.username = g.name";
            // from절의 서브쿼리는 불가능
            String query3 = "select (select avg(m1.age) from Member m1) as avgAge from Member m join Groups g";
            //enum 타입 표현
            String query4 = "select u.username, 'HELLO', TRUE from User u" +
                            "where u.type = hellojpa.jpql.MemberType.USER";
            //jqpl user groups 확인
            List<User> result2 = em.createQuery(query, User.class)
                            .setFirstResult(1)
                                    .setMaxResults(3)
                                            .getResultList();
            System.out.println("result2.size() = " + result2.size());
            for (User user1 : result2) {
                System.out.println("user = " + user1 + "group = " + user1.getGroups().getName());
            }

            em.flush();
            em.clear();
            System.out.println("=================");

            //경로 표현식 가급적 명시적 조인 사용을 하자 / m:1 fetch join -> 조회할 때 user, groups 전부다 조회한다. 즉시조회
            String query5 = "select u from User u join fetch u.groups";
            //1:m fetch join은 데이터 뻥튀기 -> DISTINCT로 중복된 team 엔티티까지 제거, 영속성에는 중복데이터가 합쳐짐 / 페이징은 사용 금지
            String query6 = "select distinct g from Groups g join fetch g.users";
            List<Groups> resultList = em.createQuery(query6, Groups.class)
                            .getResultList();

            for (Groups s : resultList) {
                System.out.println("s = " + s.getName() + "|Users = " + s.getUsers().size());
                for (User user : s.getUsers()) {
                    System.out.println("-> user = " + user);
                }
            }

            //named 장점: 쿼리 앱 시작 시점에 sql로 파싱해서 미리 갖고있음 오류를 미리 잡기 가능 == DAO, userRepository
            List<User> resultUser = em.createNamedQuery("User.findByUsername", User.class)
                    .setParameter("username", "user1")
                    .getResultList();
            for (User user : resultUser) {
                System.out.println("user = " + user);
            }

            //벌크 연산 = 한번에 많은 쿼리를 업데이트 하는 것 ex) 재고가 10개 이하인 모든 제품 10퍼 할인
            // * 주의사항: 영속성을 무시하고 db에 직접 쿼리함. so. 영속성 컨텍스트 초기화해야 함
            User user = new User();
            user.setUsername("user1");
            user.setAge(10);
            em.persist(user);
            int resultCount = em.createQuery("update User u set u.age = 20")
                    .executeUpdate();

            em.clear();
            User finduser = em.find(User.class, user.getId());
            System.out.println("resultCount = " + finduser.getAge());


            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();
    }
}
