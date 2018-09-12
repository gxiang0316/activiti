package com.xiang.activiti.repository;

import com.xiang.activiti.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;


public interface LeaveRepository extends CrudRepository<Leave,String>,
		JpaRepository<Leave,String>,JpaSpecificationExecutor<Leave>
{

}
