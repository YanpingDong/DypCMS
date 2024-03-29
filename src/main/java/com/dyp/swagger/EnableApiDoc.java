package com.dyp.swagger;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AipDocImportselector.class)
public @interface EnableApiDoc {

}
