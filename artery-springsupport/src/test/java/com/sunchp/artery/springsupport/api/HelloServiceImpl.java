package com.sunchp.artery.springsupport.api;

import com.sunchp.artery.springsupport.annotation.ArteryService;

@ArteryService(interfaceClass = HelloService.class)
public class HelloServiceImpl implements HelloService {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }

    @Override
    public String hello(Person person) {
        return "Hello! " + person.getFirstName() + " " + person.getLastName();
    }

    @Override
    public Person hello(String lastName, String firstname) {
        return new Person(lastName, firstname);
    }

}
