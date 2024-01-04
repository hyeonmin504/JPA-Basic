package hellojpa.jpql;

import hellojpa.Team;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

public class JpaMain2 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            for (int i=0 ; i<4 ; i++){
                Groups groups = new Groups();
                groups.setName("group" + i);
                em.persist(groups);

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

            //페이징 + 조인 , left join = group이 없어도 그냥 조인 시킴 , on = 조건절 , query2 = 관계없음 엔티티 관계 조간
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

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();
    }
}
