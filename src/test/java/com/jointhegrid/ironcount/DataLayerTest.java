package com.jointhegrid.ironcount;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DataLayerTest extends BaseEmbededServerSetupTest {
  
  static DataLayer d;

  @Before
  public void setupLocal() throws Exception {
    d = new DataLayer(cluster);
    d.createMetaInfo();
  }


  @Test
  public void aTest(){
    
    Workload w = new Workload();
    w.active=true;
    w.consumerGroup="mygroup";
    w.maxWorkers=4;
    w.messageHandlerName="com.myorg.myhandler";
    w.name="myworkload";
    w.properties = new HashMap<String,String>();
    w.topic="stuff";


    Workload w1 = new Workload();
    w1.active=true;
    w1.consumerGroup="mygroup1";
    w1.maxWorkers=4;
    w1.messageHandlerName="com.myorg.myhandler1";
    w1.name="myworkload1";
    w1.properties = new HashMap<String,String>();
    w1.topic="stuff1";

    d.startWorkload(w);
    Assert.assertEquals(w ,d.getWorkloads().get(0));


    IroncountWorkloadManager im = new IroncountWorkloadManager(cluster);
    WorkerThread wt = new WorkerThread(w);
    d.registerJob(im, w, wt);

   IroncountWorkloadManager im2 = new IroncountWorkloadManager(cluster);
    WorkerThread wt2 = new WorkerThread(w);
    d.registerJob(im2, w, wt2);

    JobInfo i = d.getJobInfoForWorkload(w);
    Assert.assertEquals(w.name, i.workloadName);
    Assert.assertEquals(true, i.workerIds.contains(im.getMyId().toString()));
    Assert.assertEquals(true, i.workerIds.contains(im.getMyId().toString()));

  }

}
