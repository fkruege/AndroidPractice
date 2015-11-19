package com.crappyweather.app.injection.scopes;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by fkruege on 8/25/2015.
 */

@Scope
@Retention(RUNTIME)
public @interface FragmentScope {}
