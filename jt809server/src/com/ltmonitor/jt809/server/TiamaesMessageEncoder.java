 package com.ltmonitor.jt809.server;
 
 import java.nio.charset.Charset;
 import org.apache.mina.core.buffer.IoBuffer;
 import org.apache.mina.core.session.IoSession;
 import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
 import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.ltmonitor.jt809.tool.Tools;
 
 public class TiamaesMessageEncoder extends ProtocolEncoderAdapter
 {
   private Charset charset;
 
   public TiamaesMessageEncoder(Charset charset)
   {
     this.charset = charset;
   }
 
   public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
     throws Exception
   {
     IoBuffer buf = IoBuffer.allocate(500).setAutoExpand(true);
 
     byte[] content = Tools.HexString2Bytes(message.toString());
     buf.put(content);
     buf.flip();
     out.write(buf);
   }
 }

