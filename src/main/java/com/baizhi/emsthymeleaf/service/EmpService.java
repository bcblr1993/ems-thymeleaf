package com.baizhi.emsthymeleaf.service;

import com.baizhi.emsthymeleaf.entity.Emp;

import java.util.List;

public interface EmpService {

    List<Emp> lists();


    void delete(Integer id);


    void save(Emp emp);

    Emp queryById(Integer id);

    void update(Emp emp);

}
