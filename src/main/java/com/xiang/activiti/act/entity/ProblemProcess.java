package com.xiang.activiti.act.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 工单
 * @author guixiang
 * 2018-10-18
 */
@Entity
@Table(name = "act_problemprocess")
public class ProblemProcess implements Serializable
{
    @Id
    @GeneratedValue(generator="system_uuid")
    @GenericGenerator(name="system_uuid",strategy="uuid")
    private String id;
    private String templateId;
    private String name;
    private String creator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "ProblemProcess{" +
                "id='" + id + '\'' +
                ", templateId='" + templateId + '\'' +
                ", name='" + name + '\'' +
                ", creator='" + creator + '\'' +
                '}';
    }
}
