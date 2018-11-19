package com.jfeat.am.module.payment.api.crud;

import com.baomidou.mybatisplus.plugins.Page;
import com.jfeat.am.common.constant.tips.SuccessTip;
import com.jfeat.am.common.constant.tips.Tip;
import com.jfeat.am.common.controller.BaseController;
import com.jfeat.am.common.exception.BusinessCode;
import com.jfeat.am.common.exception.BusinessException;
import com.jfeat.am.module.log.annotation.BusinessLog;
import com.jfeat.am.module.payment.constant.BillStatus;
import com.jfeat.am.module.payment.constant.PaymentBizException;
import com.jfeat.am.module.payment.services.crud.model.PaymentBillModel;
import com.jfeat.am.module.payment.services.domain.dao.QueryPaymentBillDao;
import com.jfeat.am.module.payment.services.domain.model.PaymentBillRecord;
import com.jfeat.am.module.payment.services.domain.service.PaymentBillService;
import com.jfeat.am.module.payment.services.persistence.model.PaymentBill;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
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
@RequestMapping("/api/payment/bills")
public class PaymentBillEndpoint extends BaseController {

    @Resource
    PaymentBillService paymentBillService;

    @Resource
    QueryPaymentBillDao queryPaymentBillDao;

    @GetMapping("/pay/{appId}/{orderNum}")
    @ApiOperation(value = "测试用，设置订单为支付")
    public Tip payBill(@PathVariable String appId, @PathVariable String orderNum) {
        paymentBillService.notifySuperVisor(orderNum, "测试,非真实支付，请忽略。");

        PaymentBill paymentBill = queryPaymentBillDao.selectOne(appId, orderNum);
        if (paymentBill == null) {
            throw PaymentBizException.BILL_NOT_FOUND.create();
        }
        if (!paymentBill.getStatus().equals(BillStatus.PAY_PENDING.toString())) {
            throw PaymentBizException.BILL_ALREADY_PAID.create();
        }
        paymentBill.setStatus(BillStatus.PAID.toString());
        paymentBillService.updateMaster(paymentBill);
        paymentBillService.notifyPayResult(appId, orderNum, RandomStringUtils.randomAlphabetic(10));
        return SuccessTip.create();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查看 PaymentBill", response = PaymentBillModel.class)
    public Tip getPaymentBill(@PathVariable Long id) {
        return SuccessTip.create(paymentBillService.retrieveMaster(id));
    }

    @ApiOperation(value = "PaymentBill 列表信息", response = PaymentBillRecord.class)
    @GetMapping
    public Tip queryPaymentBills(Page<PaymentBillRecord> page,
                                 @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                 @RequestParam(name = "appId", required = false) String appId,
                                 @RequestParam(name = "status", required = false) String status,
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

        PaymentBillRecord record = new PaymentBillRecord();
        record.setAppId(appId);
        record.setStatus(status);

        page.setRecords(queryPaymentBillDao.findPaymentBillPage(page, record, null, orderBy));

        return SuccessTip.create(page);
    }


}
