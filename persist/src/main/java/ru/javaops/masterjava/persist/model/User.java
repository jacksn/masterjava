package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class User extends BaseEntity {

    @Column("full_name")
    @NonNull
    private String fullName;

    @NonNull
    private String email;

    @NonNull
    private UserFlag flag;

    @Column("city_id")
    @NonNull
    private int cityId;

    public User(Integer id, String fullName, String email, UserFlag flag, int cityId) {
        this(fullName, email, flag, cityId);
        this.id = id;
    }
}
