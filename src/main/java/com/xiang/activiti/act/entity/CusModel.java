package com.xiang.activiti.act.entity;

/**
 * 流程模型
 */
public class CusModel
{
    private String name;
    private String key;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)//如果是同一个对象 直接返回true
            return true;
        if (obj == null)//如果比较的为null 直接返回false
            return false;
        if (getClass() != obj.getClass())//两者类类型不一样直接返回false
            return false;
        CusModel other = (CusModel) obj;//类型转换
        if (name == null) {//对于对象内的对象比较 由于要使用equals 也要注意避免出现空指针异常
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))//不会出现空指针异常 因为前面判断了
            return false;
        if (key == null) {//对于对象内的对象比较 由于要使用equals 也要注意避免出现空指针异常
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))//不会出现空指针异常 因为前面判断了
            return false;
        if (description == null) {//对于对象内的对象比较 由于要使用equals 也要注意避免出现空指针异常
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))//不会出现空指针异常 因为前面判断了
            return false;
        return true;
    }

//    @Override
//    public String toString() {
//        return "Model{" +
//                "name='" + name + '\'' +
//                ", key='" + key + '\'' +
//                ", description='" + description + '\'' +
//                '}';
//    }
}
