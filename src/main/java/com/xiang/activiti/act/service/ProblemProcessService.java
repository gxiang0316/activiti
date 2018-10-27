package com.xiang.activiti.act.service;

import com.xiang.activiti.act.entity.Leave;
import com.xiang.activiti.act.entity.ProblemProcess;
import com.xiang.activiti.act.repository.ProblemProcessRepository;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class ProblemProcessService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProblemProcessService.class);

    @Autowired
    private ProblemProcessRepository repository;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    /**
     * 启动请假流程
     * @param entity
     */
    public void startProcess(ProblemProcess entity)
    {

        ProcessInstance processInstance = null;
        try
        {
            LOGGER.debug("save entity: {}", entity);
            entity = repository.save(entity);
            String businessKey = entity.getId().toString();
            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(entity.getCreator());
            Map<String, Object> variables = new HashMap<String, Object>();
            //此处的"problem"key值对应act_re_procdef表中key_字段，需后期优化
            processInstance = runtimeService.startProcessInstanceByKey("problem", businessKey, variables);
            String processInstanceId = processInstance.getId();
            entity.setTemplateId(processInstanceId);
            LOGGER.debug("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[]{"leave", businessKey, processInstanceId, variables});
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            identityService.setAuthenticatedUserId(null);
        }
    }

    @Transactional(readOnly = true)
    public ProblemProcess getProblemProcess(String id)
    {
        return repository.getOne(id);
    }

}
