package com.ncf.apollodemo.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ncf.apollodemo.enums.XxlJobPathEnum;
import com.ncf.apollodemo.pojo.domain.AddXxlJob;
import com.ncf.apollodemo.pojo.domain.UpdateXxlJob;
import com.ncf.apollodemo.pojo.domain.XxlJobGroup;
import com.ncf.apollodemo.pojo.domain.XxlJobInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class XxlJobTemplate {
    @Value("${xxl.job.admin.addresses}")
    private String addresses;

    @Value("${xxl.job.admin.username}")
    private String username;

    @Value("${xxl.job.admin.password}")
    private String password;

    private String cookie;

    /**
     * 查询执行器
     *
     * @param appname
     * @param title
     * @return
     */
    public List<XxlJobGroup> listGroup(String appname, String title) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("appname", appname);
        paramMap.put("title", title);

        JSONObject pageGroup = doRequest(XxlJobPathEnum.PAGE_GROUP, paramMap);

        List<XxlJobGroup> jobGroupList = JSONUtil.toList(pageGroup.getJSONArray("data"), XxlJobGroup.class);
        return jobGroupList;
    }

    /**
     * 查询job列表
     *
     * @param jobGroup        -1
     * @param triggerStatus   -1
     * @param jobDesc
     * @param executorHandler
     * @param author
     * @return
     */
    public List<XxlJobInfo> listJob(int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("jobGroup", jobGroup);
        paramMap.put("triggerStatus", triggerStatus);
        paramMap.put("jobDesc", jobDesc);
        paramMap.put("executorHandler", executorHandler);
        paramMap.put("author", author);

        JSONObject pageJob = doRequest(XxlJobPathEnum.PAGE_JOB, paramMap);

        List<XxlJobInfo> jobList = JSONUtil.toList(pageJob.getJSONArray("data"), XxlJobInfo.class);
        return jobList;
    }

    /**
     * 条件查询一个Job
     *
     * @param jobGroup        -1
     * @param triggerStatus   -1
     * @param jobDesc
     * @param executorHandler
     * @param author
     * @return
     */
    public XxlJobInfo getOneJob(int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {
        List<XxlJobInfo> xxlJobInfos = listJob(jobGroup, triggerStatus, jobDesc, executorHandler, author);

        if (xxlJobInfos.size() > 1) {
            throw new RuntimeException(String.format("xxl-job-admin任务查询结果不唯一:%s", xxlJobInfos.toString()));
        }

        return xxlJobInfos.isEmpty() ? null : xxlJobInfos.get(0);
    }

    /**
     * 添加job
     *
     * @param addJob
     * @return
     */
    public Integer addJob(AddXxlJob addJob) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("jobGroup", addJob.getJobGroup());
        paramMap.put("jobDesc", addJob.getJobDesc());
        paramMap.put("author", addJob.getAuthor());
        paramMap.put("scheduleType", addJob.getScheduleType());
        paramMap.put("scheduleConf", addJob.getScheduleConf());
        paramMap.put("misfireStrategy", "DO_NOTHING");
        paramMap.put("executorRouteStrategy", "FIRST");
        paramMap.put("executorHandler", addJob.getExecutorHandler());
        paramMap.put("executorParam", addJob.getExecutorParam());
        paramMap.put("executorBlockStrategy", "SERIAL_EXECUTION");

        paramMap.put("executorTimeout", 0);
        paramMap.put("executorFailRetryCount", 0);
        paramMap.put("glueType", "BEAN");
        paramMap.put("glueSource", "");
        paramMap.put("glueRemark", "GLUE代码初始化");
        paramMap.put("glueUpdatetime", null);
        paramMap.put("childJobId", "");
        paramMap.put("triggerStatus", 1);
        paramMap.put("triggerLastTime", 0);
        paramMap.put("triggerNextTime", 0);

        JSONObject result = doRequest(XxlJobPathEnum.ADD, paramMap);

        return result.getInt("content");
    }

    /**
     * 更新job
     *
     * @param updateXxlJob
     */
    public void updateJob(UpdateXxlJob updateXxlJob) {
        updateJob(JSONUtil.parseObj(updateXxlJob));
    }

    private void updateJob(Map<String, Object> paramMap) {
        JSONObject result = doRequest(XxlJobPathEnum.UPDATE, paramMap);

        if (HttpStatus.HTTP_OK != result.getInt("code")) {
            throw new RuntimeException(String.format("xxl-job-admin更新Job失败:%s", result.getStr("msg")));
        }
    }

    //停止定时任务
    public void stopJob(Integer jobId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", jobId);
        JSONObject result = doRequest(XxlJobPathEnum.STOP, paramMap);
    }

    /**
     * 删除job
     *
     * @param jobId
     */
    public void removeJob(int jobId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", jobId);

        doRequest(XxlJobPathEnum.REMOVE, paramMap);
    }

    /**
     * 获取登录cookie
     *
     * @return
     */
    private String getCookie() {
        Map<String, Object> paramsMap = new HashMap();
        paramsMap.put("userName", username);
        paramsMap.put("password", password);
        HttpResponse response = HttpRequest.post(String.format("%s%s", addresses, XxlJobPathEnum.LOGIN.getPath()))
                .form(paramsMap).execute();
        if (HttpStatus.HTTP_OK != response.getStatus()) {
            throw new RuntimeException(String.format("xxl-job-admin登录失败:statusCode=%s", response.getStatus()));
        }

        List<HttpCookie> cookies = response.getCookies();

        if (cookies.isEmpty()) {
            throw new RuntimeException(String.format("xxl-job-admin登录失败:[userName=%s,password=%s]", username, password));
        }

        return cookies.stream().map(cookie -> cookie.toString()).collect(Collectors.joining());
    }

    /**
     * 远程调用xxl-job-admin
     *
     * @param xxlJobPathEnum
     * @param paramMap
     * @return
     */
    private JSONObject doRequest(XxlJobPathEnum xxlJobPathEnum, Map<String, Object> paramMap) {
        if (StrUtil.isBlank(cookie)) {
            cookie = getCookie();
        }

        HttpResponse response = HttpRequest.post(String.format("%s%s", addresses, xxlJobPathEnum.getPath()))
                .cookie(cookie).form(paramMap).execute();
        if (HttpStatus.HTTP_OK != response.getStatus()) {
            throw new RuntimeException(String.format("xxl-job-admin%s请求失败:statusCode=%s",
                    xxlJobPathEnum.getDesc(), response.getStatus()));
        }

        JSONObject result = JSONUtil.parseObj(response.body());

        Integer code = result.getInt("code");
        if (code != null && HttpStatus.HTTP_OK != code) {
            throw new RuntimeException(String.format("xxl-job-admin%s失败:msg=%s", xxlJobPathEnum.getDesc(), result.getStr("msg")));
        }

        return result;
    }


}