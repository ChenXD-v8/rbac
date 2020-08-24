package com.jeasyuicn.rbac.common;

import lombok.Data;

import java.util.List;

@Data
public class Menu {
    private Long id;
    private String text;
    private String state="open";
    private Long parentId;
    private String href;
    private List<Menu> Children;
}
