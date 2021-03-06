package com.haohao.xubei.di;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * 限定符，用于区分方法的使用
 * date：2017/12/15 13:42
 * author：Seraph
 *
 **/
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface QualifierType {

    String value() default "";

}
