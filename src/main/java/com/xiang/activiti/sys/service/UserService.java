package com.xiang.activiti.sys.service;

import com.xiang.activiti.sys.entity.User;
import com.xiang.activiti.sys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService
{
    @Autowired
    private UserRepository repository;

    public User selectByNameAndPwd(User user)
    {
        return repository.findByUserNameAndPassword(user.getUserName(),user.getPassword());
    }
}
