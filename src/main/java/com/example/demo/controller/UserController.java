package com.example.demo.controller;

import com.example.demo.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final List<User> users = new ArrayList<>(List.of(
        new User(1, "Alice", "alice@example.com"),
        new User(2, "Bob",   "bob@example.com")
    ));

    // GET all users
    @GetMapping
    public List<User> getAll() {
        return users;
    }

    // GET user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable int id) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - create user
    @PostMapping
    public User create(@RequestBody User user) {
        int nextId = users.stream().mapToInt(User::getId).max().orElse(0) + 1;
        user.setId(nextId);
        users.add(user);
        return user;
    }

    // PUT - update user
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable int id, @RequestBody User updated) {
        for (User u : users) {
            if (u.getId() == id) {
                u.setName(updated.getName());
                u.setEmail(updated.getEmail());
                return ResponseEntity.ok(u);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean removed = users.removeIf(u -> u.getId() == id);
        return removed ? ResponseEntity.noContent().build()
                       : ResponseEntity.notFound().build();
    }
}
