package com.bdyh.repository;

import com.bdyh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author BD-PC40
 * @Title: UserRepositoryJpa
 * @ProjectName docmarke
 * @Description: TODO
 * @date 2018/9/516:48
 */
public interface UserRepositoryJpa extends JpaRepository<User, String> {
}
