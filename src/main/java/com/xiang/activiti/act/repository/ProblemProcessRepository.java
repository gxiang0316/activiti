package com.xiang.activiti.act.repository;

import com.xiang.activiti.act.entity.ProblemProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ProblemProcessRepository extends CrudRepository<ProblemProcess,String>,
        JpaRepository<ProblemProcess,String> ,JpaSpecificationExecutor<ProblemProcess>
{
    //ProblemProcess findByTemplateId(String templateId);
}
