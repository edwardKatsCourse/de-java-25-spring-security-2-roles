package com.example;

import com.example.entity.Account;
import com.example.entity.AccountPassword;
import com.example.entity.RoleType;
import com.example.repository.AccountRepository;
import com.example.service.PasswordService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Scanner;

@Component
@Transactional
@AllArgsConstructor
public class Runner implements CommandLineRunner {

    private final PasswordService passwordService;
    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        // password: 123456
        // salt    : asdqf312dwqd

        // 123456  x3
        // (password * salt) x 1,000

        // Thread.sleep(100)

        // 123456 * asdqf312dwqd = result 1
        // result 1 * asdqf312dwqd

        // 24681012
        // 123456

        // 123456 -> 369121518


        // generate password hash
        // - salt


        // registration

        // transaction begin
        // - save account
        // - save account info
        // - save account roles
        // - save account image
        // - save account email verification -> failure -> transaction rollback
        // - save account password
        // transaction commit


        createUser("John Doe", "jdoe", "123456", RoleType.ADMIN);
        createUser("Sarah Stone", "sstone", "qwerty", RoleType.USER);

    }

    private void createUser(String name, String username, String password, RoleType roleType) {
        Account account = Account.builder()
                .name(name)
                .username(username)
                .role(roleType) // USER,ADMIN
                .build();

        accountRepository.save(account);
        passwordService.generateAndSavePassword(account, password);
    }





    public static void main(String[] args) {
        String password = "123456";

//        String salt = BCrypt.gensalt();
        String salt = new Scanner(System.in).nextLine();
        var hashedPassword = BCrypt.hashpw(password, salt);


        System.out.println("Password: " + password);
        System.out.println("Salt: " + salt);
        System.out.println("Hashed password: " + hashedPassword);

        // 123456 + $2a$10$BVFs2WxB4XaG.BjhFU.SiO = $2a$10$BVFs2WxB4XaG.BjhFU.SiOs89Mh1zFGV9Mf4orUx72z4qA5w1S1/W

        // Login
        // account password -> salt
        // 123456

        // $2a$10$VzN6UzeqzKl9DzwlMpiP0O

        // $2a$10$BVFs2WxB4XaG.BjhFU.SiOs89Mh1zFGV9Mf4orUx72z4qA5w1S1/W

        // $2a$10$VzN6UzeqzKl9DzwlMpiP0OeShj9gsnANl4pU5Tx9nXVUlVbRBMqPC
    }
}
