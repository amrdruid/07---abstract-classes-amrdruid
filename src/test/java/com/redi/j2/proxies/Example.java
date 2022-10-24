package com.redi.j2.proxies;

import com.redi.j2.utils.ReflectionProxy;

public class Example extends ReflectionProxy {

    public Example(int bar) {
        super(new Object[]{ bar });
    }

    public Example(Object target) {
        super(target);
    }

    @Override
    public String getTargetClassName() {
        return "com.redi.j2.Example";
    }

    public Integer foo(int number){
        if(getTarget() == null) {
            return null;
        }
        return invokeMethod("foo", new Class[]{int.class}, number);
    }

    public Integer getBar() {
        if(getTarget() == null) {
            return null;
        }
        return invokeMethod("getBar", new Class[]{});
    }

    public void setBar(int bar) {
        if(getTarget() == null) {
            return;
        }
        invokeMethod("setBar", new Class[]{int.class}, bar);
    }
}
