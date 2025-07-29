package com.gimin.jpopblog.domain.user.repository;

import com.gimin.jpopblog.domain.user.entity.Nickname;
import com.gimin.jpopblog.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

    public interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByEmail(String email);

        Optional<User> findByNicknameValue(String nicknameValue);

        boolean existsByNicknameValue(String nicknameValue);
    }
