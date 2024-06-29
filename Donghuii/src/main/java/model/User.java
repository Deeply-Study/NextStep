package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public boolean login(String password) {
        return this.password.equals(password);
    }
}
