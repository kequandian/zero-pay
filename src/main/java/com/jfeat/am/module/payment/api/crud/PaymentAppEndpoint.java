package com.jfeat.am.module.payment.api.crud;

import com.baomidou.mybatisplus.plugins.Page;
import com.jfeat.am.common.constant.tips.SuccessTip;
import com.jfeat.am.common.constant.tips.Tip;
import com.jfeat.am.common.controller.BaseController;
import com.jfeat.am.common.exception.BusinessCode;
import com.jfeat.am.common.exception.BusinessException;
import com.jfeat.am.module.log.annotation.BusinessLog;
import com.jfeat.am.module.payment.services.crud.model.PaymentAppModel;
import com.jfeat.am.module.payment.services.domain.dao.QueryPaymentAppDao;
import com.jfeat.am.module.payment.services.domain.model.PaymentAppRecord;
import com.jfeat.am.module.payment.services.domain.service.PaymentAppService;
import io.swagger.annotations.ApiOperation;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;


/**
 * <p>
 * api
 * </p>
 *
 * @author Code Generator
 * @since 2018-10-18
 */
@RestController
@RequestMapping("/api/payment/apps")
public class PaymentAppEndpoint extends BaseController {


    @Resource
    PaymentAppService paymentAppService;

    @Resource
    QueryPaymentAppDao queryPaymentAppDao;

    @PostMapping
    @ApiOperation(value = "新建 PaymentApp", response = PaymentAppModel.class)
    public Tip createPaymentApp(@RequestBody PaymentAppModel entity) {

        try {
            paymentAppService.createMaster(entity);

        } catch (DuplicateKeyException e) {
            throw new BusinessException(BusinessCode.DuplicateKey);
        }

        return SuccessTip.create(entity);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查看 PaymentApp", response = PaymentAppModel.class)
    public Tip getPaymentApp(@PathVariable Long id) {
        return SuccessTip.create(paymentAppService.retrieveMaster(id));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改 PaymentApp", response = PaymentAppModel.class)
    public Tip updatePaymentApp(@PathVariable Long id, @RequestBody PaymentAppModel entity) {
        entity.setId(id);
        return SuccessTip.create(paymentAppService.updateMaster(entity));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除 PaymentApp")
    public Tip deletePaymentApp(@PathVariable Long id) {
        return SuccessTip.create(paymentAppService.deleteMaster(id));
    }

    @ApiOperation(value = "PaymentApp 列表信息", response = PaymentAppRecord.class)
    @GetMapping
    public Tip queryPaymentApps(Page<PaymentAppRecord> page,
                                @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(name = "search", required = false) String search,
                                @RequestParam(name = "id", required = false) Long id,
                                @RequestParam(name = "appId", required = false) String appId,
                                @RequestParam(name = "appCode", required = false) String appCode,
                                @RequestParam(name = "createTime", required = false) Date createTime,
                                @RequestParam(name = "status", required = false) String status,
                                @RequestParam(name = "note", required = false) String note,
                                @RequestParam(name = "orderBy", required = false) String orderBy,
                                @RequestParam(name = "sort", required = false) String sort) {
        if (orderBy != null && orderBy.length() > 0) {
            if (sort != null && sort.length() > 0) {
                String pattern = "(ASC|DESC|asc|desc)";
                if (!sort.matches(pattern)) {
                    throw new BusinessException(BusinessCode.BadRequest.getCode(), "sort must be ASC or DESC");//此处异常类型根据实际情况而定
                }
            } else {
                sort = "ASC";
            }
            orderBy = "`" + orderBy + "`" + " " + sort;
        }
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        PaymentAppRecord record = new PaymentAppRecord();
        record.setId(id);
        record.setAppId(appId);
        record.setAppCode(appCode);
        record.setCreateTime(createTime);
        record.setStatus(status);
        record.setNote(note);

        page.setRecords(queryPaymentAppDao.findPaymentAppPage(page, record, search, orderBy));

        return SuccessTip.create(page);
    }


}
