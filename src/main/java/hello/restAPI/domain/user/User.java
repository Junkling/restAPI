package hello.restAPI.domain.user;

import hello.restAPI.domain.heart.Heart;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountId;

    private String password;

    private String nickName;

    @ColumnDefault("null")
    private String role;

    @OneToOne(mappedBy = "user")
    private Heart heart;

    private Boolean quite;

    @CreationTimestamp
    private Timestamp created;
    @UpdateTimestamp
    private Timestamp updated;

}
