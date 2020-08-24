package com.jeasyuicn.rbac.common;

import lombok.Data;

@Data
public class Menu2 {
    private Long id;
    private String text;
    private String state="open";
    private Long parentId;
    private String href;
}
