/*
Copyright 2011 Edward Capriolo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.jointhegrid.ironcount.eventtofile;

import com.jointhegrid.ironcount.manager.MessageHandler;
import com.jointhegrid.ironcount.manager.WorkerThread;
import com.jointhegrid.ironcount.manager.Workload;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

import kafka.message.Message;
import kafka.message.MessageAndMetadata;
import org.apache.log4j.Logger;

public class MessageToFileHandler implements MessageHandler {


  final static Logger logger = Logger.getLogger(MessageToFileHandler.class.getName());
  FileWriter fw ;
  WorkerThread parent;
  
  public MessageToFileHandler(){

  }

  @Override
  public void setWorkload(Workload w) {

  }

  @Override
  public void handleMessage(MessageAndMetadata<Message> m) {
    String s = getMessage(m.message());
    try {
      System.out.println(s);
      fw.write(s+"\n");
      fw.flush();
    } catch (IOException ex) {
     logger.error(ex);
    }
  }

  @Override
  public void setWorkerThread(WorkerThread wt) {
    parent=wt;
    try {
      fw = new FileWriter(new File("/tmp/ic_"+parent.getWtId()));
    } catch (IOException ex) {
      logger.error(ex);
    }
  }

  @Override
  public void stop() {
    try {
      fw.close();
    } catch (IOException ex) {
      logger.error(ex);
    }
  }

  @Override
  public void finalize () throws Throwable{
    super.finalize();
    this.stop();
  }

  public static String getMessage(Message message) {
    ByteBuffer buffer = message.payload();
    byte[] bytes = new byte[buffer.remaining()];
    buffer.get(bytes);
    return new String(bytes);
  }
}