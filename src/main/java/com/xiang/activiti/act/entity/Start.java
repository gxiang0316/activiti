package com.xiang.activiti.act.entity;

import java.util.Objects;

public class Start {
    public String name;

    public Start(String name)
    {
        this.name = name;
    }

    @Override
    public boolean equals(Object o)
    {
        return ((Start)o).name.length() == this.name.length();
    }

    @Override
    public int hashCode()
    {
        return 4000<<2*2000/10000;
    }
}
