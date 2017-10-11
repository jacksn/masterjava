package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class Group extends BaseEntity {

    @NonNull
    private String name;

    @NonNull
    private GroupType type;

    @Column("project_id")
    @NonNull
    private int projectId;

    public Group(Integer id, String name, GroupType type, int projectId) {
            this(name, type, projectId);
        this.id = id;
    }
}
