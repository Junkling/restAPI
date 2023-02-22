package hello.restAPI.web.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.net.http.HttpHeaders;
import java.util.List;

@Service
public class HeaderCheck {

    private String splitRole(String header) {
        String[] s = header.split(" ");
        System.out.println("s[0] = " + s[0]);
        return s[0];
    }
    public Long splitId(String header) {
        String[] s = header.split(" ");
        Long aLong = Long.valueOf(s[1]);
        System.out.println("aLong = " + aLong);
        return aLong;
    }

    public Boolean checkAuth(String header) {
        String role = splitRole(header);
        if (role.equals("Realtor") || role.equals("Lessor") || role.equals("Lessee")) {
            System.out.println("role = " + role);
            return true;
        }
        System.out.println("role = " + role);
        return false;
    }

}
