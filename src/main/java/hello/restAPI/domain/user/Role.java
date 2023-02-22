package hello.restAPI.domain.user;

import lombok.Getter;

@Getter
public enum Role {
    Realtor("Realtor","공인중개사"),
    Lessor("Lessor","임대인"),
    Lessee("Lessee", "임차인");

    private String code;
    private String name;

    Role(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
