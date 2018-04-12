package com.sunchp.artery.springsupport.api;

import com.sunchp.artery.springsupport.annotation.ArteryReferer;

@ArteryReferer
public interface HelloService {
    String hello(String name);

    String hello(Person person);

    Person hello(String lastName, String firstname);
}
