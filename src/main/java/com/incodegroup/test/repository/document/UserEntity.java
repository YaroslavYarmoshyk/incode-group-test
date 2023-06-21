package com.incodegroup.test.repository.document;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@Document(collection = "Users")
public class UserEntity {
    @Id
    private String id;
    @Indexed(unique=true)
    private String email;
    private String password;
    private String name;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserEntity)) {
            return false;
        }
        final UserEntity other = (UserEntity) o;
        return id != null && Objects.equals(id, other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
