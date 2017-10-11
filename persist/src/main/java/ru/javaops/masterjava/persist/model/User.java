package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class User extends BaseEntity {
    @Column("full_name")
    private @NonNull String fullName;
    private @NonNull String email;
    private @NonNull UserFlag flag;
    @Column("city_id")
    private @NonNull Integer cityId;

    public User(Integer id, String fullName, String email, UserFlag flag, Integer cityId) {
        this(fullName, email, flag, cityId);
        this.id=id;
    }
}