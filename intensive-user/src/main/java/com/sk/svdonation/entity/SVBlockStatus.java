package com.sk.svdonation.entity;

public enum SVBlockStatus {
    /**
     * Off Chain에서 Commit 되고 블록체인에 TX를 보내지 않아도 되는 상태
     */
    NONE, 
    /**
     * Off Chain에서 Commit 되고 블록체인에 TX를 보낸 상태
     */
    SENDED, 
    /**
     * Off Chain에서 Commit 되고 블록체인에서 Mining이 완료된 상태
     */
    OK,
    /**
     * Off Chain에서 Commit 되고 블록체인에서 Mining이 실패된 상태
     */
    FAIL,
    /**
     * FAIL상태의 데이터가 Off Chain에서 Rollback 되고 블록체인에서 Mining이 실패된 상태
     */
    ROLLBACKED
}