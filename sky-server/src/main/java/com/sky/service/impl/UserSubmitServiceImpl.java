package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.UserSubmitService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.webSocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/18 16:44
 */
@Service
public class UserSubmitServiceImpl implements UserSubmitService {

    @Autowired
    private UserSubmitMapper userSubmitMapper;

    @Autowired
    private addressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 地址簿为空，购物车数据为空直接返回
        AddressBook addressById = addressBookMapper.getAddressById(ordersSubmitDTO.getAddressBookId());
        if(addressById == null){
            throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.viewShoppingCart(BaseContext.getCurrentId());
        if(shoppingCarts == null || shoppingCarts.size() == 0){
            throw new OrderBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }


        // 清空用户当前购物车
        shoppingCartMapper.cleanShoppingCart(BaseContext.getCurrentId());


        // 往order表里插入数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);

        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(BaseContext.getCurrentId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);

        // 其次，联合其他表进行查询，填充相关冗余字段
        orders.setPhone(addressById.getPhone());
        orders.setAddress(addressById.getProvinceName() + addressById.getCityName() + addressById.getDistrictName() + addressById.getDetail());
        orders.setConsignee(addressById.getConsignee());

        userSubmitMapper.addData(orders);

        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(orders.getId());
        orderSubmitVO.setOrderTime(orders.getOrderTime());
        orderSubmitVO.setOrderAmount(orders.getAmount());
        orderSubmitVO.setOrderNumber(orders.getNumber());

        // 查询购物车表，将用户的购物车数据导入order表
        OrderDetail orderDetail = new OrderDetail();
        for (ShoppingCart shoppingCart : shoppingCarts){
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailMapper.addData(orderDetail);
        }

        return orderSubmitVO;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
        // 跳过微信支付流程
        JSONObject jsonObject = new JSONObject();

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = userSubmitMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        Map<Object, Object> objectObjectHashMap = new HashMap<>();

        objectObjectHashMap.put("type", 1);
        objectObjectHashMap.put("orderId", ordersDB.getId());
        objectObjectHashMap.put("content", "订单号" + outTradeNo);

        String s = JSONObject.toJSONString(objectObjectHashMap);

        webSocketServer.sendToAllClient(s);

        userSubmitMapper.update(orders);
    }

    @Override
    public OrderVO orderDetail(Long id) {
        Orders orders = userSubmitMapper.getByID(id);

        // 拷贝order属性
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);

        // 查询并封装订单原型List

        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(id);
        orderVO.setOrderDetailList(orderDetails);

        return orderVO;
    }

    @Override
    public PageResult historyOrders(Integer page, Integer pageSize, Integer status) {
        PageHelper.startPage(page, pageSize);

        Page<OrderVO> page1 = orderDetailMapper.historyOrders(BaseContext.getCurrentId(), status);


        for (int i = 0; i < page1.getResult().size(); i++) {
            OrderVO orderVO = page1.getResult().get(i);

            List<OrderDetail> byOrderId = orderDetailMapper.getByOrderId(orderVO.getId());
            orderVO.setOrderDetailList(byOrderId);
        }

        PageResult pageResult = new PageResult();
        pageResult.setTotal(page1.getTotal());
        pageResult.setRecords(page1.getResult());

        return pageResult;
    }

    @Override
    public void cancel(Long id) {
        userSubmitMapper.cancel(id, Orders.CANCELLED);
    }

    // 这个再来一单太过于生硬，应该是添加到购物车，让用户决定再加不加
//    @Override
//    public void repetition(Long id) {
//        Orders orders = userSubmitMapper.getByID(id);
//        orders.setNumber(String.valueOf(System.currentTimeMillis()));
//        orders.setStatus(Orders.PENDING_PAYMENT);
//        orders.setOrderTime(LocalDateTime.now());
//        orders.setPayStatus(Orders.UN_PAID);
//
//        userSubmitMapper.addData(orders);
//
//        List<OrderDetail> byOrderId = orderDetailMapper.getByOrderId(id);
//
//        // 批量插入
//        orderDetailMapper.batchAddData(byOrderId);
//    }

    @Override
    public void repetition(Long id) {
        List<OrderDetail> byOrderId = orderDetailMapper.getByOrderId(id);

        for(OrderDetail orderDetail : byOrderId){
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart);
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.addShoppingCart(shoppingCart);
        }
    }

    @Override
    public void reminder(Long id) {
//        bjectObjectHashMap.put("type", 1);
//        objectObjectHashMap.put("orderId", ordersDB.getId());
//        objectObjectHashMap.put("content", "订单号" + outTradeNo);
        Orders byID = userSubmitMapper.getByID(id);

        if(byID == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        Map map = new HashMap<>();
        map.put("type", 2);
        map.put("orderId", id);
        map.put("content", "订单号" + byID.getNumber());

        String s = JSONObject.toJSONString(map);

        webSocketServer.sendToAllClient(s);
    }
}
