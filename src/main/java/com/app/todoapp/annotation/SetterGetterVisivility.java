package com.app.todoapp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public @interface SetterGetterVisivility {
}
