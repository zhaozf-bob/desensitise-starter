/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.webrun.demos.web;

import com.example.desensitise.config.DesensitiseProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;

@Controller
public class BasicController {

    @Autowired
    private DesensitiseProperties desensitiseProperties;

    // http://127.0.0.1:8080/user
    @RequestMapping("/user")
    @ResponseBody
    public User user() {
        User user = new User();
        user.setName("theonefx");
        user.setAge(666);
        user.setIdentifiers(Arrays.asList("123456", "654321"));

        School school1 = new School();
        school1.setName("school1");
        User user2 = new User();
        user2.setName("user2");
        school1.setUser(user2);
        School school2 = new School();
        school2.setName("school2");
        school2.setUsers(Arrays.asList(user2));
        user.setSchools(Arrays.asList(school1, school2));

        HashMap<String, School> map = new HashMap<>();
        School school3 = new School();
        BeanUtils.copyProperties(school1, school3);
        School school4 = new School();
        BeanUtils.copyProperties(school1, school4);
        map.put(school1.toString(), school3);
        map.put(school2.toString(), school3);
        user.setSchoolMap(map);

        HashMap<School, String> schoolMap2 = new HashMap<>();
        schoolMap2.put(school1, "school1");
        schoolMap2.put(school2, "school2");
        user.setSchoolMap2(schoolMap2);

        return user;
    }

    // http://127.0.0.1:8080/hello?name=lisi
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return "Hello " + name;
    }

    // http://127.0.0.1:8080/save_user?name=newName&age=11
    @RequestMapping("/save_user")
    @ResponseBody
    public String saveUser(User u) {
        return "user will save: name=" + u.getName() + ", age=" + u.getAge();
    }

    // http://127.0.0.1:8080/html
    @RequestMapping("/html")
    public String html() {
        return "index.html";
    }

    @ModelAttribute
    public void parseUser(@RequestParam(name = "name", defaultValue = "unknown user") String name
            , @RequestParam(name = "age", defaultValue = "12") Integer age, User user) {
        user.setName("zhangsan");
        user.setAge(18);
    }
}
