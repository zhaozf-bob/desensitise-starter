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

import java.util.List;
import java.util.Map;

public class User {

    private String name;

    private Integer age;

    private List<String> identifiers;

    private List<School> schools;

    private Map<String, School> schoolMap;

    private Map<School, String> schoolMap2;

    public Map<School, String> getSchoolMap2() {
        return schoolMap2;
    }

    public void setSchoolMap2(Map<School, String> schoolMap2) {
        this.schoolMap2 = schoolMap2;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    public List<School> getSchools() {
        return schools;
    }

    public void setSchools(List<School> schools) {
        this.schools = schools;
    }

    public Map<String, School> getSchoolMap() {
        return schoolMap;
    }

    public void setSchoolMap(Map<String, School> schoolMap) {
        this.schoolMap = schoolMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
