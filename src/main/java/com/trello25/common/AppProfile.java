package com.trello25.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.context.annotation.Profile;

@Profile({"dev", "prod"})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppProfile {

}
