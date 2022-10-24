package com.redi.j2;

import com.redi.j2.fixtures.Fixtures;
import com.redi.j2.proxies.Example;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Step1Tests {

    @Test
    void task_1_1_shouldDefineExampleClass() {

        // given - a class we want the students to implement
        Example example;

        // when - we check if it exists
        example = Fixtures.createExample();

        // then - it should exist
        assertTrue(example.existsClass(), "Example class is not defined");

        // and - it should not be abstract
        assertFalse(example.isAbstract(), "Example should not be abstract");
    }

    @Test
    void task_1_2_shouldHaveBarProperty() {

        // given - an Example
        Example example = Fixtures.createExample();

        // when - we check if it has the 'bar' property

        // then - it should exist
        assertTrue(example.hasProperty("bar"), "Property 'bar' is not defined");

        // and - it should have the correct type
        assertTrue(example.isPropertyOfType("bar", int.class), "Property 'bar' should of type int");

        // and - it should have private access
        assertTrue(example.isPropertyPrivate("bar"), "Property 'bar' should have private access");
    }

    @Test
    void task_1_3_shouldHaveGetterForBar() {

        // given - an Example
        Example example = Fixtures.createExample();

        // when - we check if it has the 'getBar' method

        // then - it should exist
        assertTrue(example.hasMethod("getBar"), "Method 'getBar' is not defined");

        // and - it should have the correct return type
        assertTrue(example.isMethodReturnType(int.class, "getBar"), "Method 'getBar' should return an int");

        // and - it should have public access
        assertTrue(example.isMethodPublic("getBar"), "Method 'getBar' should be public");
    }

    @Test
    void task_1_3_shouldHaveSetterForBar() {

        // given - an Example
        Example example = Fixtures.createExample();

        // when - we check if it has the 'setBar' method

        // then - it should exist
        assertTrue(example.hasMethod("setBar", int.class), "Method 'setBar' is not defined");

        // and - it should have the correct return type
        assertTrue(example.isMethodReturnType(void.class, "setBar", int.class), "Method 'setBar' should return void");

        // and - it should have public access
        assertTrue(example.isMethodPublic("setBar", int.class), "Method 'setBar' should be public");
    }

    @Test
    void task_1_4_shouldHaveParametrizedConstructor() {

        // given - an Example
        Example example = Fixtures.createExample();

        // when - we check if it has the parametrized constructor

        // then - it should exist
        assertTrue(example.hasConstructor(int.class),
                "Constructor with parameters is not defined as specified in the exercise (int)");

        // and - it should have public access
        assertTrue(example.isConstructorPublic(int.class),
                "The constructor should be public");
    }

    @Test
    void task_1_5_shouldHaveFooMethod() {

        // given - an Example
        Example example = Fixtures.createExample();

        // when - we check if it has the 'foo' method

        // then - it should exist
        assertTrue(example.hasMethod("foo", int.class), "Method 'foo(int)' is not defined");

        // and - it should have the correct return type
        assertTrue(example.isMethodReturnType(int.class, "foo", int.class), "Method 'foo' should return an int");

        // and - it should have public access
        assertTrue(example.isMethodPublic("foo", int.class), "Method 'foo' should be public");
    }

    @Test
    void task_1_5_shouldAddBarToFooParameter() {

        // given - an Example
        Example example = new Example(1);

        // when - we call the 'foo' method
        int result = example.foo(1);

        // then - the result should be the sum of "bar" and the number passed as an argument
        assertEquals(2, result, "The foo method should add the value of 'bar' to the specified parameter");
    }
}
