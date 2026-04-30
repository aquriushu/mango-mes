package com.mango.bkl.constant;

import java.util.Random;

public class RedisConst {

    // ---------------------- 员工 Redis key and expire_time ------------------------------------------------------
    public static final String EMPLOYEE_HASH_KEY_PREFIX = "employee:hash:";

    private static final int EMPLOYEE_EXPIRE_BASE_TIME = 60 * 30;
    public static final int EMPLOYEE_EXPIRE_TIME = EMPLOYEE_EXPIRE_BASE_TIME + new Random().nextInt(EMPLOYEE_EXPIRE_BASE_TIME);

    // ---------------------- 物料 Redis key and expire_time ------------------------------------------------------
    public static final String MATERIAL_HASH_KEY_PREFIX = "material:hash:";

    private static final int MATERIAL_EXPIRE_BASE_TIME = 60 * 30;
    public static final int MATERIAL_EXPIRE_TIME = MATERIAL_EXPIRE_BASE_TIME + new Random().nextInt(MATERIAL_EXPIRE_BASE_TIME);

}
