package com.alinesno.infra.common.web.adapter.login.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.web.adapter.dto.LoginBodyDto;
import com.alinesno.infra.common.web.adapter.dto.menus.Menu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class CommonLoginController {

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBodyDto loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = UUID.randomUUID().toString() ;
        ajax.put(TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {

        Map<String, Object> data = new HashMap<>();
        // 将数据填充到data中...
        data.put("permissions", new String[]{"*:*:*"});

        Map<String, Object> user = new HashMap<>();
        user.put("createBy", "admin");
        user.put("createTime", "2023-04-23 16:11:38");
        user.put("updateBy", null);
        user.put("updateTime", null);
        user.put("remark", "管理员");
        user.put("userId", 1);
        user.put("deptId", 103);
        user.put("userName", "admin");
        user.put("nickName", "AIP技术团队");
        user.put("email", "aip-team@163.com");
        user.put("phonenumber", "15888888888");
        user.put("sex", "1");
        user.put("avatar", "");
        user.put("password", "");
        user.put("status", "0");
        user.put("delFlag", "0");
        user.put("loginIp", "");
        user.put("loginDate", "2023-09-21T16:54:12.000+08:00");

        Map<String, Object> dept = new HashMap<>();
        dept.put("createBy", null);
        dept.put("createTime", null);
        dept.put("updateBy", null);
        dept.put("updateTime", null);
        dept.put("remark", null);
        dept.put("deptId", 103);
        dept.put("parentId", 101);
        dept.put("ancestors", "0,100,101");
        dept.put("deptName", "研发部门");
        dept.put("orderNum", 1);
        dept.put("leader", "AIP技术团队");
        dept.put("phone", null);
        dept.put("email", null);
        dept.put("status", "0");
        dept.put("delFlag", null);
        dept.put("parentName", null);
        dept.put("children", new Object[]{});

        user.put("dept", dept);

        Map<String, Object> role = new HashMap<>();
        role.put("createBy", null);
        role.put("createTime", null);
        role.put("updateBy", null);
        role.put("updateTime", null);
        role.put("remark", null);
        role.put("roleId", 1);
        role.put("roleName", "超级管理员");
        role.put("roleKey", "admin");
        role.put("roleSort", 1);
        role.put("dataScope", "1");
        role.put("menuCheckStrictly", false);
        role.put("deptCheckStrictly", false);
        role.put("status", "0");
        role.put("delFlag", null);
        role.put("flag", false);
        role.put("menuIds", null);
        role.put("deptIds", null);
        role.put("permissions", null);
        role.put("admin", true);

        user.put("roles", new Object[]{role});

        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", user.get("roles"));
        ajax.put("permissions", data.get("permissions"));

        return ajax;
    }

    /**
     * 获取路由信息
     * G:\lgb-work\lad\new\alinesno-infra-data-stream\alinesno-infra-data-stream-admin\src\views\flink\systemConfig\list.vue
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {

        Menu dashboardMenu = new Menu("Dashboard", "/dashboard", false, "noRedirect", "Layout", true, new Menu.Meta("仪盘表", "dashboard", false, null), List.of(
                new Menu("Dashboard", "index", false, false , "dashboard", new Menu.Meta("概览", "dashboard", false, null))
        ));

        Menu flinkMenu = new Menu("Flink", "/flink", false, "noRedirect", "Layout", true, new Menu.Meta("实时计算", "monitor", false, null), List.of(
                new Menu("Application", "flink/application/list", false, false , "flink/application/list/:context?", new Menu.Meta("应用管理", "peoples", false, null)),
                new Menu("JobManage", "flink/jobManage/list", false, false , "flink/jobManage/list/:context?", new Menu.Meta("任务列表", "peoples", false, null)),
                new Menu("JobRunLog", "flink/jobRunLog/list", false, false , "flink/jobRunLog/list", new Menu.Meta("任务日志", "peoples", false, null)),
                new Menu("AlarmLog", "flink/alarmLog/list", false, false , "flink/alarmLog/list", new Menu.Meta("告警日志", "peoples", false, null)),
                new Menu("SavepointBackup", "flink/savepointBackup/list", false, false , "flink/savepointBackup/list", new Menu.Meta("保存点", "peoples", false, null)),
                new Menu("JobHistory", "flink/jobHistory/list", false, false , "flink/jobHistory/list/:flag?/:context?/:jobConfigId?", new Menu.Meta("任务历史", "peoples", false, null)),

                new Menu("CreateSqlStreamingTask", "flink/jobManage/sqltask", true, false , "flink/jobManage/sqltask/:flag?/:context?/:data?", new Menu.Meta("创建SQL流任务", "peoples", false, null)),
                new Menu("UpdateSqlStreamingTask", "flink/jobManage/sqltask", true, false , "flink/jobManage/sqltask/:flag?/:context?/:data?", new Menu.Meta("编辑SQL流任务", "peoples", false, null)),
                new Menu("ViewSqlStreamingTask", "flink/jobManage/sqltask", true, false , "flink/jobManage/sqltask/:flag?/:context?/:data?", new Menu.Meta("查看SQL流任务", "peoples", false, null)),

                new Menu("CreateJarTask", "flink/jobManage/jartask", true, false , "flink/jobManage/jartask/:flag?/:context?/:data?", new Menu.Meta("创建JAR任务", "peoples", false, null)),
                new Menu("UpdateJarTask", "flink/jobManage/jartask", true, false , "flink/jobManage/jartask/:flag?/:context?/:data?", new Menu.Meta("编辑JAR批任务", "peoples", false, null)),
                new Menu("ViewJarTask", "flink/jobManage/jartask", true, false , "flink/jobManage/jartask/:flag?/:context?/:data?", new Menu.Meta("查看JAR批任务", "peoples", false, null))
        ));

        Menu systemMenu = new Menu("System", "/system", false, "noRedirect", "Layout", true, new Menu.Meta("系统配置", "system", false, null), List.of(
                new Menu("JarFile", "flink/jarFile/list", false, false , "flink/jarFile/list", new Menu.Meta("二方库", "peoples", false, null)),
                new Menu("SystemConfig", "flink/systemConfig/list", false, false , "flink/systemConfig/list", new Menu.Meta("系统配置", "peoples", false, null))
        ));

        List<Menu> menus = List.of(dashboardMenu, flinkMenu , systemMenu ) ;
        String jsonString = JSON.toJSONString(menus, SerializerFeature.WriteMapNullValue);

        return AjaxResult.success(JSONArray.parseArray(jsonString)) ;
    }

}
