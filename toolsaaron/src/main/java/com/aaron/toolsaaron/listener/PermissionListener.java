package com.aaron.toolsaaron.listener;

import java.util.List;

/**
 * @author Aaron
 * @description: 权限申请回调的接口
 */
public interface PermissionListener {

    /**
     * 申请成功
     */
    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
