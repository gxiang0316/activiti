package com.xiang.activiti.act.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "oa_leave")
public class Leave implements Serializable {

    private static final long serialVersionUID = 1L;
    private String processInstanceId;
    private String userId;
    private String reason;
    @Id
    private Long id;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Leave{" +
                "processInstanceId='" + processInstanceId + '\'' +
                ", userId='" + userId + '\'' +
                ", reason='" + reason + '\'' +
                ", id=" + id +
                '}';
    }
}
