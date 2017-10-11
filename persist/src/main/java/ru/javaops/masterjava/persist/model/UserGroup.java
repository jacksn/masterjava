package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class UserGroup {
    @NonNull @Column("user_id") private Integer userId;
    @NonNull @Column("group_id") private Integer groupId;
}