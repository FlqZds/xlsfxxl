package com.yunting.client.common.ano;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD})
@Documented
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface CheckLogin {

}
