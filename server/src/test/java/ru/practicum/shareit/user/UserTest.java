package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void testUserCreation() {
        User user = User.builder()
                .id(1L)
                .name("Alex")
                .email("alex@email.com")
                .build();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("Alex");
        assertThat(user.getEmail()).isEqualTo("alex@email.com");
    }

    @Test
    public void testUserEquality() {
        User user1 = new User(1L, "Alex", "alex@email.com");
        User user2 = new User(1L, "Alex", "alex@email.com");
        User user3 = new User(2L, "Alex", "alex@email.com");

        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);
    }

    @Test
    public void testUserHashCode() {
        User user1 = new User(1L, "Alex", "alex@email.com");
        User user2 = new User(1L, "Alex", "alex@email.com");

        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    public void testUserToString() {
        User user = new User(1L, "Alex", "alex@email.com");

        String expectedString = "User(id=1, name=Alex, email=alex@email.com)";
        assertThat(user.toString()).isEqualTo(expectedString);
    }
}
