package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class City extends BaseEntity {

    @NonNull
    private String ref;
    @NonNull
    private String name;

    public City(Integer id, String ref, String name) {
        this(ref, name);
        this.id = id;
    }
}