package com.px.aicodemother.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.px.aicodemother.model.dto.app.AppQueryRequest;
import com.px.aicodemother.model.entity.App;
import com.px.aicodemother.model.vo.app.AppVO;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author px
 */
public interface AppService extends IService<App> {

    /**
     * 获取应用视图对象。
     *
     * @param app 应用实体对象
     * @return 应用视图对象
     */
    AppVO getAppVO(App app);

    /**
     * 获取查询条件对象。
     *
     * @param appQueryRequest 应用查询参数
     * @return 查询条件对象
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    List<AppVO> getAppVOList(List<App> appList);
}
