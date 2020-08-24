package com.jeasyuicn.rbac.common;

import lombok.Data;
@Data
public final class JsonResult {
    private boolean success=true;
    private String msg;
    public JsonResult(){

    }
    public JsonResult(String msg){
        this.msg=msg;
    }
    public static JsonResult success(){
        return new JsonResult();
    }
    public static JsonResult success(String msg){
        return new JsonResult(msg);
    }
    public static JsonResult error(){
        JsonResult jr=new JsonResult();
        jr.success=false;
        return jr;
    }
    public static JsonResult error(String msg){
        JsonResult jr=new JsonResult();
        jr.msg=msg;
        jr.success=false;
        return jr;
    }
}
