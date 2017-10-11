package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Group extends BaseEntity {

    @NonNull private String name;
    @NonNull private GroupType type;
    @NonNull @Column("project_id") private int projectId;

    public Group(Integer id, String name, GroupType type, int projectId) {
        this(name, type, projectId);
        this.id = id;
    }
}
