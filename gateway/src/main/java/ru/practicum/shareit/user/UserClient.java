package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import ru.practicum.shareit.user.dto.UserDto;


@Service
public class UserClient extends BaseClient {

    private final String baseUrl;

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(createRestTemplate(builder, serverUrl));
        this.baseUrl = serverUrl + "/users";
    }

    private static RestTemplate createRestTemplate(RestTemplateBuilder builder, String serverUrl) {
        return builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/users"))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();
    }


    public ResponseEntity<Object> getUserById(@PathVariable("userId") Long userId) {
        return get("/" + userId, userId);

    }

    public ResponseEntity<Object> getAllUsers() {
        return get("");
    }


    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        return post("", userDto);
    }


    public ResponseEntity<Object> updateUser(@PathVariable("userId") Long userId,
                                             @RequestBody UserDto userDto) {
        return patch("/" + userId, userDto);
    }

    public ResponseEntity<Object> deleteUser(@PathVariable("userId") Long userId) {
        return delete("/" + userId, userId);
    }
}
