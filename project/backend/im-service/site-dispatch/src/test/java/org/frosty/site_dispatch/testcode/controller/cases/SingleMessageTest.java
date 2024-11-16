package org.frosty.site_dispatch.testcode.controller.cases;

import org.apache.commons.lang3.tuple.Pair;
import org.frosty.common.response.Response;
import org.frosty.site_dispatch.entity.MessagePacketDTO;
import org.frosty.site_dispatch.entity.MessageType;
import org.frosty.site_dispatch.entity.SingleMessageDTO;
import org.frosty.site_dispatch.mapper.UnackedMapper;
import org.frosty.site_dispatch.mapper.UnposedMapper;
import org.frosty.site_dispatch.testcode.controller.SiteMsgAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@IdempotentControllerTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
public class SingleMessageTest {
    private final int timeout = 10000, smallWait = 500;
    // 必须要启动msg_deliver!
    @Autowired
    private SiteMsgAPI siteMsgApi;
    @Autowired
    private UnackedMapper unackedMapper;
    @Autowired
    private UnposedMapper unposedMapper;

    private MessagePacketDTO receivePacket(BlockingDeque<Response> flow, int timeout) throws
            InterruptedException {
        return siteMsgApi.getRcvdPacketMessage(flow.poll(timeout, TimeUnit.MILLISECONDS));
    }

    private SingleMessageDTO receive(BlockingDeque<Response> flow, int timeout) throws
            InterruptedException {
        return siteMsgApi.getRcvdSingleMessage(flow.poll(timeout, TimeUnit.MILLISECONDS));
    }

    private Pair<BlockingDeque<Response>, Disposable> getFlow(Flux<Response> flux) {
        BlockingDeque<Response> flow = new LinkedBlockingDeque<>();
        Disposable closing = flux.subscribe(flow::add);
        return Pair.of(flow, closing);
    }

    private void checkUnposed(long mid, SingleMessageDTO msg) {
        SingleMessageDTO dbRcvd = unposedMapper.selectById(mid);
        siteMsgApi.checkMessage(dbRcvd, msg, mid, null);
    }

    private void checkUnacked(long mid, SingleMessageDTO msg) {
        SingleMessageDTO dbRcvd = unackedMapper.selectById(mid);
        siteMsgApi.checkMessage(dbRcvd, msg, mid, null);
    }

    @Test
    public void testNoConnection() throws Exception {
//        long uid = siteMsgApi.addUser("longzhi");
        long uid = 1;
        var msg = siteMsgApi.getSimpleNewMessage(uid, false);
        long mid = siteMsgApi.pushSuccess(msg);
        SingleMessageDTO dbRcvd = unposedMapper.selectById(mid);
        siteMsgApi.checkMessage(dbRcvd, msg, mid, null);
    }

    @Test
    public void testNoConnectionMultipleSend() throws Exception {
//        long uid = siteMsgApi.addUser("longzhi");
        long uid = 1;
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            var msg = siteMsgApi.getSimpleNewMessage(uid, false, String.valueOf(random.nextDouble()));
            long mid = siteMsgApi.pushSuccess(msg);
            SingleMessageDTO dbRcvd = unposedMapper.selectById(mid);
            siteMsgApi.checkMessage(dbRcvd, msg, mid, null);
        }
    }

    @Test
    public void testNormalTransmission() throws Exception {
//        long uid = siteMsgApi.addUser("longzhi");
        long uid = 1;
        var flux = siteMsgApi.register(uid);
        var flow = getFlow(flux).getKey();
        var msg = siteMsgApi.getSimpleNewMessage(uid, false);
        receivePacket(flow, timeout);
        Thread.sleep(smallWait);
        long mid = siteMsgApi.pushSuccess(msg);
        var rcvd = receive(flow, timeout);

        siteMsgApi.checkMessage(rcvd, msg, mid, MessageType.NEW);
    }

    @Test
    public void testUnconnectedTransmission() throws Exception {
        //先push后register
//        long uid = siteMsgApi.addUser("longzhi");
        long uid = 1;
        var msg = siteMsgApi.getSimpleNewMessage(uid, false);

        long mid = siteMsgApi.pushSuccess(msg);
        checkUnposed(mid, msg);

        var flux = siteMsgApi.register(uid);
        var flow = getFlow(flux).getKey();
        var initialPacket = receivePacket(flow, timeout);

        assert unposedMapper.selectById(mid) == null;
        assert initialPacket.getUnposed().size() == 1;
        var rcvd = initialPacket.getUnposed().get(0);
        siteMsgApi.checkMessage(rcvd, msg, mid, null);
    }

    @Test
    public void testAck() throws Exception {
        //initialized
//        long uid = siteMsgApi.addUser("longzhi");
        long uid = 1;
        var flux = siteMsgApi.register(uid);
        var flow = getFlow(flux).getKey();
        var msg = siteMsgApi.getSimpleNewMessage(uid, true);
        receivePacket(flow, timeout);
        Thread.sleep(smallWait);

        //push ack msg
        long mid = siteMsgApi.pushSuccess(msg);
        var rcvd = receive(flow, timeout);
        siteMsgApi.checkMessage(rcvd, msg, mid, null);
        checkUnacked(mid, msg);
        //ack
        siteMsgApi.ackSuccess(mid);
        assert unackedMapper.selectById(mid) == null;
    }

    @Test
    public void testDisconnectThenAck() throws Exception {
        //initialized
//        long uid = siteMsgApi.addUser("longzhi");
        long uid = 1;
        var flux = siteMsgApi.register(uid);
        var pair = getFlow(flux);
        var flow = pair.getKey();
        var close = pair.getRight();
        var msg = siteMsgApi.getSimpleNewMessage(uid, true);
        receivePacket(flow, timeout);
        Thread.sleep(smallWait);

        //push ack msg
        long mid = siteMsgApi.pushSuccess(msg);
        var rcvd = receive(flow, timeout);
        siteMsgApi.checkMessage(rcvd, msg, mid, null);
        checkUnacked(mid, msg);
        //close
        close.dispose();
        //reopen
        flux = siteMsgApi.register(uid);
        flow = getFlow(flux).getKey();
        rcvd = receivePacket(flow, timeout).getUnacked().get(0);
        siteMsgApi.checkMessage(rcvd, msg, mid, null);
        //ack
        siteMsgApi.ackSuccess(mid);
        Thread.sleep(smallWait);
        assert unackedMapper.selectById(mid) == null;
    }
}
