package space.jachen.gmall.common.log.api;


import space.jachen.gmall.domain.log.SysOperLog;

/**
 *
 * 保存操作日志的接口
 *
 * @author JaChen
 * @date 2022/12/26 12:01
 */
public interface OperLogService {

    /**
     * 保存操作日志
     * @param sysOperLog
     */
    void saveSysOperLog(SysOperLog sysOperLog);

}
