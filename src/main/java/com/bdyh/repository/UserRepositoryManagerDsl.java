package com.bdyh.repository;

import com.bdyh.entity.QUser;
import com.bdyh.entity.User;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
/**
　　* @Description: TODO
　　* @param ${tags} 
　　* @return ${return_type} 
　　* @throws
　　* @author hechangtao
　　* @date 2018/9/19 18:30
　　*/
@Component
@Transactional
public class UserRepositoryManagerDsl {
    private final EntityManager entityManager;
    private final UserRepositoryJpa userRepositoryJpa;

    @Autowired
    public UserRepositoryManagerDsl(EntityManager entityManager, UserRepositoryJpa userRepositoryJpa) {
        this.entityManager = entityManager;
        this.userRepositoryJpa = userRepositoryJpa;
    }

    public User findUser(String account) {
        if(StringUtils.isEmpty(account)){
            return null;
        }
        QUser qUser = QUser.user;
        JPAQuery<User> queryFactory = new JPAQuery<>(entityManager);

        List<User> users = queryFactory.select(qUser)
                .from(qUser)
                .where(qUser.account.eq(account))
                .fetch();

        return (users != null && users.size() > 0) ? users.get(0) : null;
    }
}
