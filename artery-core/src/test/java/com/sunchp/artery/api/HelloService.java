package com.sunchp.artery.api;

public interface HelloService {
    String hello(String name);

    String hello(Person person);

    Person hello(String lastName, String firstname);
}
