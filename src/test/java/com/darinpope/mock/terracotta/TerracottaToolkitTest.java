package com.darinpope.mock.terracotta;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.terracotta.api.ClusteringToolkit;
import org.terracotta.util.ClusteredAtomicLong;

import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

@ContextConfiguration("classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TerracottaToolkitTest {

    @Resource
    private ClusteringToolkit mockToolkit;

    @Resource
    private ClusteredAtomicLong mockAtomicLong;

    @Before
    public void setUpBeforeClass() throws Exception {

        BlockingQueue queue = new LinkedBlockingQueue();

        when(mockToolkit.getBlockingQueue((String) any())).thenReturn(queue);
        when(mockToolkit.getAtomicLong((String) any())).thenReturn(mockAtomicLong);
    }

    @Test
    public void testBlockingQueue() throws Exception {
        BlockingQueue<byte[]> testQueue = mockToolkit.getBlockingQueue("testQueue");
        assertNotNull(testQueue);

        byte[] queuePutValue = SerializationUtils.serialize("qqq");
        testQueue.put(queuePutValue);
        assertEquals("queue size is not 1",1,testQueue.size());

        byte[] queueTakeValue = testQueue.take();
        assertNotNull(queueTakeValue);
        assertEquals("Value is not qqq", "qqq", (String) SerializationUtils.deserialize(queueTakeValue));
    }

    @Test
    public void testAtomicLong() throws Exception {
        ClusteredAtomicLong testAtomicLong = mockToolkit.getAtomicLong("testAtomicLong");
        long localValue = testAtomicLong.getAndIncrement();
        localValue = testAtomicLong.getAndIncrement();
        verify(testAtomicLong, times(2)).getAndIncrement();
    }

}
