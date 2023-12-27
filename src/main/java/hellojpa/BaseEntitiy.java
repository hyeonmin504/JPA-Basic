package hellojpa;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass //테이블과 관계없이 단순히 공통으로 사용하는 맵핑정보 모음 추상 클래스로 작성
public abstract class BaseEntitiy {

    @Column(name = "INSERT_MEMBER")
    private String createBy;
    private LocalDateTime createdDate;
    private String lastModeifiedDate;
    private LocalDateTime lastModifiredDate;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModeifiedDate() {
        return lastModeifiedDate;
    }

    public void setLastModeifiedDate(String lastModeifiedDate) {
        this.lastModeifiedDate = lastModeifiedDate;
    }

    public LocalDateTime getLastModifiredDate() {
        return lastModifiredDate;
    }

    public void setLastModifiredDate(LocalDateTime lastModifiredDate) {
        this.lastModifiredDate = lastModifiredDate;
    }
}
