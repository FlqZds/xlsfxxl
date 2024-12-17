package com.yunting.common.utils;

import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * @author csk
 * @date 2024/7/22
 * @Description
 */
public class SpringRollBackUtil {

    /**
     * 事务回滚机制
     */
    public static void rollBack() {
        try {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

