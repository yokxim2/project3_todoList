package com.sparta.project3_todolist.utility;

import lombok.Getter;

@Getter
public class Page {
    private Long pageNum;
    private Long pageSize;

    public Page(Long pageNum, Long pageSize) {
        this.pageNum = (pageNum != null) ? pageNum : 1L;
        this.pageSize = (pageSize != null) ? pageSize : 10L;
    }

    public Long getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
