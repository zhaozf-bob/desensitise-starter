package com.example.webrun.demos.service;

import com.example.desensitise.model.DesensitiseConfig;
import com.example.desensitise.service.IDesensitiseConfigProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DesensitiseConfigProviderImpl implements IDesensitiseConfigProvider {
    @Override
    public List<DesensitiseConfig> match(List<String> keys) {
        if (keys.contains("/user")) {
            ArrayList<DesensitiseConfig> list = new ArrayList<>();
            list.add(new DesensitiseConfig("/user", "name", "中文名"));
            list.add(new DesensitiseConfig("/user", "age", "number"));
            list.add(new DesensitiseConfig("/user", "identifiers", "中文名"));
            list.add(new DesensitiseConfig("/user", "schools.name", "中文名"));
            list.add(new DesensitiseConfig("/user", "schools.user.name", "中文名"));
            list.add(new DesensitiseConfig("/user", "schools.user.age", "number"));
            list.add(new DesensitiseConfig("/user", "schools.users.name", "中文名"));
            list.add(new DesensitiseConfig("/user", "schools.users.age", "number"));
            list.add(new DesensitiseConfig("/user", "schoolMap.name", "中文名"));
            list.add(new DesensitiseConfig("/user", "schoolMap2", "中文名"));
            return list;
        }
        return null;
    }
}
