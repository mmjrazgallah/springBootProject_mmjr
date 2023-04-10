package com.sip.services;

import com.sip.entities.Article;
import com.sip.entities.Provider;
import com.sip.entities.Role;
import com.sip.entities.User;
import com.sip.repositories.RoleRepository;
import com.sip.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User verifierUser(String email,String password) {
        User user = userRepository.findByEmail(email);
        if(user!=null)
            if (user.getPassword()!=bCryptPasswordEncoder.encode(password))
                user=null;

            return user;
    }

    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(0);
        userRepository.save(user);
    }
    public void updateUser(User user) {
        userRepository.save(user);
    }
    public List<User> listUser() {
        return userRepository.findAll();
    }
    public User userById(int id) {
    User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid User Id:" + id));
    return user;
    }
    public long  nbrUsers() {
        return userRepository.count();
    }
    public long  nbrUsersDisables() {
        return userRepository.usersActive(0).size();
    }

}

