package com.xiang.activiti.sys.repository;

import com.xiang.activiti.sys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,String>, JpaRepository<User,String>, JpaSpecificationExecutor<User>
{
    User findByUserNameAndPassword(String userName,String password);
}
