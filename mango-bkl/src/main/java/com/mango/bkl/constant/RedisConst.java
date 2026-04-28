package com.mango.bkl.constant;

import java.util.Random;

public class RedisConst {

    public static final String EMPLOYEE_HASH_KEY_PREFIX = "employee:hash:";

    private static final int EMPLOYEE_EXPIRE_BASE_TIME = 60 * 30;
    public static final int EMPLOYEE_EXPIRE_TIME = EMPLOYEE_EXPIRE_BASE_TIME + new Random().nextInt(EMPLOYEE_EXPIRE_BASE_TIME);

}
