package ru.javaops.masterjava.persist.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class City extends BaseEntity {

    @NonNull
    private String code;

    @NonNull
    private String name;

    public City(Integer id, String code, String name) {
        this(code, name);
        this.id = id;
    }
}
