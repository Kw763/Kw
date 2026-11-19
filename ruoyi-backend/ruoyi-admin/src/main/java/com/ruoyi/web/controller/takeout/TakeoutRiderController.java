package com.ruoyi.web.controller.takeout;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.takeout.domain.TakeoutDeliveryTask;
import com.ruoyi.takeout.service.ITakeoutRiderService;

/**
 * 外卖骑手端控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/takeout/rider")
public class TakeoutRiderController extends BaseController
{
    @Autowired
    private ITakeoutRiderService takeoutRiderService;

    /**
     * 当前骑手任务列表
     */
    @PreAuthorize("@ss.hasPermi('takeout:rider:delivery:list')")
    @GetMapping("/tasks")
    public TableDataInfo taskList(TakeoutDeliveryTask query)
    {
        startPage();
        List<TakeoutDeliveryTask> list = takeoutRiderService.selectMyTaskList(query);
        return getDataTable(list);
    }

    /**
     * 配送任务详情
     */
    @PreAuthorize("@ss.hasPermi('takeout:rider:delivery:query')")
    @GetMapping("/tasks/{taskId}")
    public AjaxResult taskDetail(@PathVariable Long taskId)
    {
        return success(takeoutRiderService.selectTaskById(taskId));
    }

    /**
     * 骑手接单
     */
    @PreAuthorize("@ss.hasPermi('takeout:rider:delivery:accept')")
    @Log(title = "外卖-骑手接单", businessType = BusinessType.UPDATE)
    @PostMapping("/tasks/{taskId}/accept")
    public AjaxResult acceptTask(@PathVariable Long taskId)
    {
        return toAjax(takeoutRiderService.acceptTask(taskId));
    }

    /**
     * 骑手到店
     */
    @PreAuthorize("@ss.hasPermi('takeout:rider:delivery:arrive')")
    @Log(title = "外卖-骑手到店", businessType = BusinessType.UPDATE)
    @PostMapping("/tasks/{taskId}/arrive")
    public AjaxResult arriveTask(@PathVariable Long taskId)
    {
        return toAjax(takeoutRiderService.arriveTask(taskId));
    }

    /**
     * 骑手取餐
     */
    @PreAuthorize("@ss.hasPermi('takeout:rider:delivery:pickup')")
    @Log(title = "外卖-骑手取餐", businessType = BusinessType.UPDATE)
    @PostMapping("/tasks/{taskId}/pickup")
    public AjaxResult pickupTask(@PathVariable Long taskId)
    {
        return toAjax(takeoutRiderService.pickupTask(taskId));
    }

    /**
     * 骑手送达
     */
    @PreAuthorize("@ss.hasPermi('takeout:rider:delivery:deliver')")
    @Log(title = "外卖-骑手送达", businessType = BusinessType.UPDATE)
    @PostMapping("/tasks/{taskId}/deliver")
    public AjaxResult deliverTask(@PathVariable Long taskId)
    {
        return toAjax(takeoutRiderService.deliverTask(taskId));
    }
}
