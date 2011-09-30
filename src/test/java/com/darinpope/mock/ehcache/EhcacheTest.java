package com.darinpope.mock.ehcache;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.cluster.CacheCluster;
import net.sf.ehcache.concurrent.CacheLockProvider;
import net.sf.ehcache.store.TerracottaStore;
import net.sf.ehcache.terracotta.ClusteredInstanceFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


@ContextConfiguration("classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class EhcacheTest {

    @Resource
    private ClusteredInstanceFactory mockClusteredInstanceFactory;

    @Resource
    private CacheCluster mockCacheCluster;

    @Resource
    private TerracottaStore mockTerracottaStore;

    @Resource
    private CacheLockProvider mockCacheLockProvider;

    @Before
    public void before() throws Exception {
        when(mockClusteredInstanceFactory.createStore((Ehcache) any())).thenReturn(mockTerracottaStore);
        when(mockClusteredInstanceFactory.getTopology()).thenReturn(mockCacheCluster);
        when(mockTerracottaStore.getInternalContext()).thenReturn(mockCacheLockProvider);
    }

    @Test
    public void testCacheGet() throws Exception {
    }
}
